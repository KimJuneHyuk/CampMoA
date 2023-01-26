const form = window.document.getElementById('form');
console.log(form['title']);
console.log(form['bid']);
console.log(form['content']);
console.log(form['email']);

const Warning = {
    getElement: () => document.getElementById('warning'),
    hide: () => Warning.getElement().classList.remove('visible'),
    show: (text) => {
        Warning.getElement().innerText = text;
        Warning.getElement().classList.add('visible');
    }
};

form['back'].addEventListener('click', () => {
    window.history.length < 2 ? window.close() : window.history.back();
});


form.onsubmit = e => {
    e.preventDefault();
    Warning.hide();

    if (form['title'].value === '') {
        Warning.show('제목을 입력해 주세요.');
        form['title'].focus();
        return false;
    }

    if (form['content'].value === '') {
        Warning.show('질문 내용을 입력해 주세요.');
        form['content'].focus();
        return false;
    }


    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('boardValue', form['bid'].value);
    formData.append('title', form['title'].value);
    formData.append('content', form['content'].value);
    formData.append('userEmail', form['email'].value);
    // formData.append('userEmail', form['email'].value);

    xhr.open('POST', './write');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson['result']);

                switch (responseJson['result']) {
                    case 'not_allowed':
                        Warning.show('게시글을 작성할 수 있는 권항이 없거나 로그아웃 되었습니다. 확인 후 다시 시도 해 주세요. ')
                        break;
                    case 'success':
                        alert('작성 완료 ( success )');
                        const aid = responseJson['aid'];
                        window.location.href = `read?page=1&aid=${aid}`;
                        break;
                    default:
                        Warning.show('알수 없는 이유로 게시글 작성을 하지 못하였스빈다.  \'POST\', \'./write\'')
                            break;
                }
            } else {
                Warning.show('서버와 통신하지 못하였습니다. // \'POST\', \'./write\'')
            }
        }
    };
    xhr.send(formData);
};