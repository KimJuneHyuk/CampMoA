let continents = [];
let countries = [];
let regions = [];
// 배열 초기화 값.

const continentContainer = window.document.getElementById('continentContainer');
// html 의 대륙 컨테이너 div 를 상수 지정.

const drawContinents = () => {
    // 상수 익명 함수 생성.
    // 대륙 컨테이너를 ''; 아무것도 없게 만들어주었다.
    continentContainer.innerHTML = '';
    //continents.를 for 돌렸을떄,
    continents.forEach(continent => {
        // continentElement 는 새로운 createElement('div')를 만들어주고,
        const continentElement = window.document.createElement('div');
        // 클래스 이름으로 continent 지어주었다.
        continentElement.classList.add('continent');
        // continentElement.dataset.value 는 continent['value'] 값으로 지정한다. AM,EA....
        continentElement.dataset.value = continent['value'];
        // 만들어진 div 의 innerText 는 DB의 text 로 한다. (동아시아...)
        continentElement.innerText = continent['text'];

        // 만들어진 div가 클릭이 발생했을 경우. 클릭이 발생한 div의 종류중 속성으로 selected 가
        // 들어가 있으면 함수를 빠져나오게 하고,
        continentElement.addEventListener('click', e => {
            if (e.target.getAttribute('selected') === 'selected') {
                return;
            }
            drawSubs(continents.find(x => x['value'] === e.target.dataset.value));
            setSelectedContinent(e.target.dataset.value);
        });
        continentContainer.append(continentElement);
    });
    continentContainer.querySelector(':scope > .continent:first-of-type').setAttribute('selected', 'selected');
};


const getSelectedContinent = () => {
    const selectedValue = continentContainer.querySelector(':scope > .continent[selected]').dataset.value;
    return continents.find(x => x['value'] === selectedValue);
};

const setSelectedContinent = (value) => {
    continentContainer
        .querySelectorAll(':scope > .continent')
        .forEach(x => x.removeAttribute('selected'));
    continentContainer
        .querySelector(`:scope > .continent[data-value="${value}"]`).setAttribute('selected', 'selected');
};

const getSelectedCountry = () => {
    const selectedContinent = getSelectedContinent();
    const selectedCountryElement = subContainer.querySelector('.country[data-value][selected]');
    return countries.find(x => x['continentValue'] === selectedContinent['value'] && x['value'] === selectedCountryElement.dataset.value)
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

const subContainer = window.document.getElementById('subContainer');
const drawSubs = (continent) => {
    subContainer.innerHTML = '';
    countries.filter(x => x['continentValue'] === continent['value'])
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
                .filter(x => x['continentValue'] === country['continentValue'] && x['countryValue'] === country['value'])
                .forEach(region => {
                    const regionElement = window.document.createElement('div');
                    regionElement.classList.add('region');
                    regionElement.dataset.value = region['value'];
                    regionElement.innerText = region['text'];
                    regionElement.addEventListener('click', e => {
                        if (e.target.getAttribute('selected') === 'selected') {
                            return;
                        }
                        setSelectedCountry(e.target.parentNode.parentNode.querySelector('.country[data-value]').dataset.value);
                        setSelectedRegion(e.target.dataset.value);
                    });
                    regionContainerElement.append(regionElement);
                });


            subElement.append(countryElement, regionContainerElement);


            subContainer.append(subElement);
        });
    subContainer.querySelector(':scope > .sub:first-of-type > .country').setAttribute('selected', 'selected');
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

const accompanyContainer = window.document.getElementById('accompanyContainer');

const appendSearch = () => {
//    함수 appendSearch 만들었다.
    const selectedContinent = getSelectedContinent();
    const selectedCountry = getSelectedCountry();
    const selectedRegion = getSelectedRegion();

    // cover.show('동행을 찾고 있습니다. 잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('continentValue', selectedContinent['value']);
    formData.append('countryValue', selectedCountry['value']);
    formData.append('lastArticleId', lastArticleId);
    if (selectedRegion !== null && selectedRegion !== undefined) {
        formData.append('value', selectedRegion['value']);
    }
    xhr.open('POST', './');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                const currentDate = new Date(xhr.getResponseHeader('Date'));

                for (let article of responseJson['accompanyArticles']) {
                    if (accompanyContainer.querySelector(`.accompany[data-id="${article['index']}"]`) !== null) {
                        continue;
                    }
                    const accompanyElement = window.document.createElement('a');
                    accompanyElement.classList.add('accompany');
                    accompanyElement.dataset.id = article['index'];
                    accompanyElement.setAttribute('href', `./read/${article['index']}`);

                    const imgElement = window.document.createElement('img');
                    imgElement.classList.add('cover-image');
                    imgElement.setAttribute('alt', '');
                    imgElement.setAttribute('src', `./cover-image/${article['index']}`);

                    const locationContainerElement = window.document.createElement('span');
                    locationContainerElement.classList.add('location-container');

                    const locationIconContainerElement = window.document.createElement('span');
                    locationIconContainerElement.classList.add('icon-container');

                    const locationTextElement = window.document.createElement('span');
                    locationTextElement.classList.add('text');
                    locationTextElement.innerText = regions.find(x =>
                        x['continentValue'] === article['continentValue'] &&
                        x['countryValue'] === article['countryValue'] &&
                        x['value'] === article['regionValue'])['text'];

                    const locationDurationElement = window.document.createElement('span');
                    const dateFrom = new Date(article['dateFrom']);
                    const dateTo = new Date(article['dateTo']);
                    const dateFromText = `${((dateFrom.getMonth() + 1) < 10 ? '0' : '') + (dateFrom.getMonth() + 1)}/${(dateFrom.getDate() < 10 ? '0' : '') + dateFrom.getDate()}`;
                    const dateToText = `${((dateTo.getMonth() + 1) < 10 ? '0' : '') + (dateTo.getMonth() + 1)}/${(dateTo.getDate() < 10 ? '0' : '') + dateTo.getDate()}`;
                    locationDurationElement.classList.add('duration');
                    locationDurationElement.innerText = `${dateFromText} - ${dateToText}`;
                    locationContainerElement.append(locationIconContainerElement, locationTextElement, locationDurationElement);

                    const titleContainerElement = window.document.createElement('span');
                    titleContainerElement.classList.add('title-container');
                    const titleStatusElement = window.document.createElement('span');
                    titleStatusElement.classList.add('status');
                    if (dateFrom.getTime() < currentDate.getTime()) {
                        titleStatusElement.classList.add('expired');
                        titleStatusElement.innerText = '모집마감';
                    } else {
                        titleStatusElement.innerText = '모집중';
                    }

                    const titleTitleElement = window.document.createElement('span');
                    titleTitleElement.classList.add('title');
                    titleTitleElement.innerText = article['title'];
                    titleContainerElement.append(titleStatusElement, titleTitleElement);

                    const contentElement = window.document.createElement('span');
                    contentElement.classList.add('content');
                    contentElement.innerText = article['content'];
                    // console.log(article['content'])

                    const nameElement = window.document.createElement('span');
                    nameElement.classList.add('name');
                    nameElement.innerText = article['userName'];

                    accompanyElement.append(imgElement, locationContainerElement, titleContainerElement, contentElement, nameElement);
                    accompanyContainer.append(accompanyElement);

                    lastArticleId = article['index'];
                }
                if(responseJson['accompanyArticles'].length === 8) {
                    more.classList.add('visible');
                }
                else {
                    more.classList.remove('visible');
                }
            } else {
                alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.')
            }
        }
    };
    xhr.send(formData);
};
let lastArticleId = -1;

const xhr = new XMLHttpRequest();
// cover.show('지역 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.');
xhr.open('PATCH', './');
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
            // 밑에 로그 지우고 디자인 하고 탭 컨테이너 만들기
            appendSearch();

            console.log('불러온 대륙 : ' + continents.length + '개');
            console.log('불러온 국가 : ' + countries.length + '개');
            console.log('불러온 지역 : ' + regions.length + '개');
            console.log('지역 중 대륙이 동아시아 인 것만 뽑으면?');
            regions.filter(x => x['continentValue'] === 'EA').forEach(region => {
                console.log(`이름 : ${region['text']}`);
            });
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            window.history.back();
        }
    }
};
xhr.send();


const form = window.document.getElementById('form');
form.onsubmit = e => {
    e.preventDefault();
    lastArticleId = -1;
    accompanyContainer.innerText = '';
    appendSearch();
}

const more = window.document.getElementById('more');
more.addEventListener('click', () => {
    appendSearch();
});




















