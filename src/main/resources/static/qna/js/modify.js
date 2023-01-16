const form = window.document.getElementById('form');
console.log(form['title']);
console.log(form['bid']);
console.log(form['content']);
console.log(form['email']);
// const page = window.document.getElementById('page');

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
    formData.append('title', form['title'].value);
    formData.append('content', form['content'].value);

    xhr.open('PATCH', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'no_such_article':
                        Warning.show('존재하지 않는 게시글은 수정할 수 없습니다.')
                        break;
                    case 'not-allowed':
                        Warning.show('로그인을 하지 않은 유저는 게시글을 수정할 수 없습니다.');
                        break;
                    case 'success':
                        confirm('수정하시겠습니까?');
                        const aid = responseJson['aid'];
                        const page = responseJson['page']
                        window.location.href = `read?page=${page}&aid=${aid}`;
                        break;
                    default:
                        Warning.show('알수 없는 이유로 게시글 수정을 하지 못하였습니다.  //  //  xhr.open(\'PATCH\', window.location.href');
                            break;
                }
            } else {
                Warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.    //  xhr.open(\'PATCH\', window.location.href);');
            }
        }
    };
    xhr.send(formData);
};