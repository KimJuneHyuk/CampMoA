const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);

let continents = [];
let countries = [];
let regions = [];

const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
};

const continentContainer = window.document.getElementById('continentContainer');
const drawContinents = () => {
    continentContainer.innerHTML = '';
    continents.forEach(continent => {
        const continentElement = window.document.createElement('div');
        continentElement.classList.add('continent');
        continentElement.dataset.value = continent['value'];
        continentElement.innerText = continent['text'];
        continentElement.addEventListener('click', e => {
            if (e.target.getAttribute('selected') === 'selected') {
                return;
            }
            drawSubs(continents.find(x => x['value'] === e.target.dataset.value));
            setSelectedContinent(e.target.dataset.value);
        });
        continentContainer.append(continentElement);
    });
    continentContainer
        .querySelector(':scope > .continent:first-of-type')
        .setAttribute('selected', 'selected');
};
const getSelectedContinent = () => {
    const selectedValue = continentContainer
        .querySelector(':scope > .continent[selected]')
        ?.dataset.value;
    return continents.find(x => x['value'] === selectedValue);
};
const setSelectedContinent = (value) => {
    continentContainer
        .querySelectorAll(':scope > .continent')
        .forEach(x => x.removeAttribute('selected'));
    const continentElement = continentContainer.querySelector(`:scope > .continent[data-value="${value}"]`);
    continentElement.click();
    continentElement.setAttribute('selected', 'selected');
};

const subContainer = window.document.getElementById('subContainer');
const drawSubs = (continent) => {
    subContainer.innerHTML = '';
    countries
        .filter(x => x['continentValue'] === continent['value'])
        .forEach(country => {
            const subElement = window.document.createElement('div');
            subElement.classList.add('sub');
            const countryElement = window.document.createElement('div');
            countryElement.classList.add('country');
            countryElement.dataset.value = country['value'];
            countryElement.innerText = country['text'];
            countryElement.addEventListener('click', e => {
                setSelectedCountry(e.target.dataset.value);
            });

            const regionContainerElement = window.document.createElement('div');
            regionContainerElement.classList.add('region-container');
            regions
                .filter(x => x['continentValue'] === country['continentValue'] &&
                    x['countryValue'] === country['value'])
                .forEach(region => {
                    const regionElement = window.document.createElement('div');
                    regionElement.classList.add('region');
                    regionElement.dataset.value = region['value'];
                    regionElement.innerText = region['text'];
                    regionElement.addEventListener('click', e => {
                        if (e.target.getAttribute('selected') === 'selected') {
                            return;
                        }
                        setSelectedCountry(e.target
                            .parentNode
                            .parentNode
                            .querySelector('.country[data-value]')
                            .dataset.value);
                        setSelectedRegion(e.target.dataset.value);
                    });
                    regionContainerElement.append(regionElement);
                });

            subElement.append(countryElement, regionContainerElement);
            subContainer.append(subElement);
        });
    setSelectedCountry(subContainer
        .querySelector(':scope > .sub:first-of-type > .country')
        .dataset.value);
};
const getSelectedCountry = () => {
    const selectedContinent = getSelectedContinent();
    const selectedCountryElement = subContainer.querySelector('.country[data-value][selected]');
    return countries.find(x =>
        x['continentValue'] === selectedContinent['value'] &&
        x['value'] === selectedCountryElement.dataset.value);
};
const setSelectedCountry = (value) => {
    subContainer
        .querySelectorAll('.region[data-value]')
        .forEach(x => x.removeAttribute('selected'));
    subContainer
        .querySelectorAll('.country[data-value]')
        .forEach(x => x.removeAttribute('selected'));
    subContainer
        .querySelector(`.country[data-value="${value}"]`)
        ?.setAttribute('selected', 'selected');
    subContainer
        .querySelector(`.country[data-value="${value}"]`)
        .parentNode
        .querySelector('.region[data-value]')
        .setAttribute('selected', 'selected');
};

const getSelectedRegion = () => {
    const selectedContinent = getSelectedContinent()['value'];
    const selectedCountry = getSelectedCountry()['value'];
    const selectedRegion = subContainer.querySelector('.region[selected]')?.dataset.value ?? null;
    if (selectedRegion === null) {
        return null;
    }
    return regions.find(x =>
        x['continentValue'] === selectedContinent &&
        x['countryValue'] === selectedCountry &&
        x['value'] === selectedRegion);
};
const setSelectedRegion = (value) => {
    subContainer
        .querySelectorAll('.region[data-value]')
        .forEach(x => x.removeAttribute('selected'));
    const selectedCountryValue = getSelectedCountry()['value'];
    subContainer.querySelector(`.country[data-value=${selectedCountryValue}]`)
        .parentNode
        .querySelector(`.region[data-value="${value}"]`)
        .setAttribute('selected', 'selected');
};

const xhr = new XMLHttpRequest();
// cover.show('지역 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.');
xhr.open('PATCH', '../');
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        // cover.hide();
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseJson = JSON.parse(xhr.responseText);
            continents = responseJson['accompanyContinents'];
            countries = responseJson['accompanyCountries'];
            regions = responseJson['accompanyRegions'];
            drawContinents();
            drawSubs(getSelectedContinent());
            loadArticle();
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            window.history.back();
        }
    }
};
xhr.send();

const form = window.document.getElementById('form');
const capValue = window.document.getElementById('capValue');
form['capacity'].addEventListener('input', () => {
    capValue.innerText = form['capacity'].value + '명';
});

const noCoverImage = window.document.getElementById('noCoverImage');
noCoverImage.addEventListener('click', () => {
    form['coverImage'].click();
});

const coverImagePreview = window.document.getElementById('coverImagePreview');
coverImagePreview.addEventListener('click', () => {
    form['coverImage'].click();
});
form['coverImage'].addEventListener('input', () => {
    if ((form['coverImage'].files?.length ?? 0) === 0) {
        noCoverImage.classList.add('visible');
        coverImagePreview.removeAttribute('src');
        return;
    }
    coverImagePreview.setAttribute('src', window.URL.createObjectURL(form['coverImage'].files[0]));
    noCoverImage.classList.remove('visible');
});

form['back'].addEventListener('click', () => {
    window.location.href = `../read/${id}`;
    // window.history.back();
});

form.onsubmit = e => {
    e.preventDefault();

    warning.hide();
    if (form['dateFrom'].value === '') {
        warning.show('시작 날짜를 선택해 주세요.');
        form['dateFrom'].focus();
        return false;
    }
    if (form['dateTo'].value === '') {
        warning.show('종료 날짜를 선택해 주세요.');
        form['dateTo'].focus();
        return false;
    }
    const dateFrom = Date.parse(form['dateFrom'].value);
    const dateTo = Date.parse(form['dateTo'].value);
    if (dateTo - dateFrom < 0) {
        warning.show('종료 날짜는 시작 날짜와 같은 날이거나 미래여야 합니다.');
        form['dateTo'].focus();
        return false;
    }
    if (form['title'].value === '') {
        warning.show('제목을 입력해 주세요.');
        form['title'].focus();
        return false;
    }
    if (editor.getData() === '') {
        warning.show('내용을 입력해 주세요.');
        form['content'].focus();
        return false;
    }
    // cover.show('동행 글을 수정하고 있어요.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('continentValue', getSelectedContinent()['value']);
    formData.append('countryValue', getSelectedCountry()['value']);
    formData.append('regionValue', getSelectedRegion()['value']);
    formData.append('capacity', form['capacity'].value);
    formData.append('dateFromStr', form['dateFrom'].value);
    formData.append('dateToStr', form['dateTo'].value);
    formData.append('title', form['title'].value);
    formData.append('content', editor.getData());
    if ((form['coverImage'].files?.length ?? 0) > 0) {
        formData.append('coverImageFile', form['coverImage'].files[0]);
    }
    xhr.open('POST', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('동행 글을 수정하였습니다.');
                        window.location.href = `../read/${id}`;
                        break;
                    default:
                        warning.show('알 수 없는 이유로 동행 글을 수정하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};


let editor;
// 빈 editor 생성.
const loadArticle = () => {
    // const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);
    // cover.show('동행 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('PATCH', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                const dateFromObj = new Date(responseJson['dateFrom']);
                const dateToObj = new Date(responseJson['dateTo']);
                setSelectedContinent(responseJson['continentValue']);
                setSelectedCountry(responseJson['countryValue']);
                setSelectedRegion(responseJson['regionValue']);
                coverImagePreview.setAttribute('src', `../cover-image/${responseJson['index']}`);
                capValue.innerText = `${responseJson['capacity']}명`;
                form['capacity'].value = responseJson['capacity'];
                form['dateFrom'].value = `${dateFromObj.getFullYear()}-${dateFromObj.getMonth() + 1 < 10 ? '0' : ''}${dateFromObj.getMonth() + 1}-${dateFromObj.getDate() < 10 ? '0' : ''}${dateFromObj.getDate()}`;
                form['dateTo'].value = `${dateToObj.getFullYear()}-${dateToObj.getMonth() + 1 < 10 ? '0' : ''}${dateToObj.getMonth() + 1}-${dateToObj.getDate() < 10 ? '0' : ''}${dateToObj.getDate()}`;
                form['title'].value = responseJson['title'];
                form['content'].value = responseJson['content'];
                ClassicEditor.create(form['content'], {
                    simpleUpload: {
                        uploadUrl: '../image'
                    }
                }).then(x => {
                    editor = x;
                });
            } else if (xhr.status === 403) {
                alert('ㅋ');
                window.location.href = 'https://www.google.com/search?q=더+배우고+와라+애송이';
            } else if (xhr.status === 404) {
                alert('존재하지 않는 동행입니다.');
                if (window.history.length > 0) {
                    window.history.back();
                } else {
                    window.close();
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시후 다시 시도해 주세요.');
                if (window.history.length > 0) {
                    window.history.back();
                } else {
                    window.close();
                }
            }
        }
    };
    xhr.send();
};





