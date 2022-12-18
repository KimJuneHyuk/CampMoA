const Form = document.getElementById('loginForm');
const Warning = {
    getElement: () => document.getElementById('warning'),
    hide: () => Warning.getElement().classList.remove('visible'),
    show: (text) => {
        Warning.getElement().innerText = text;
        Warning.getElement().classList.add('visible');
    }
};

Form.onsubmit = e => {
    e.preventDefault();

    if (Form['email'].value === '') {
        Warning.show('이메일을 입력해 주세요.');
        Form['email'].focus();
        return false;
    }

    if (!new RegExp('^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$').test(Form['email'].value)) {
        Warning.show('올바른 이메일을 입력해 주세요.');
        Form['email'].focus();
        Form['email'].select();
        return false;
    }

    if (Form['password'].value === '') {
        Warning.show('비밀번호를 입력해 주세요.');
        Form['password'].focus();
        return false;
    }

    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(Form['password'].value)) {
        Warning.show('올바른 비밀번호를 입력해 주세요.');
        Form['password'].focus();
        Form['password'].select();
        return false;
    }


    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', Form['email'].value);
    formData.append('password', Form['password'].value);

    xhr.open('POST', './userLogin');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson['result'])
                switch (responseJson['result']) {
                    case 'success':
                        window.location.href ='/'
                        break;
                    case 'suspended':
                        Warning.show('이용이 정지된 회원입니다.')
                        break;
                    case 'resigned':
                        Warning.show('해당 회원은 탈퇴를 진행한 회원 입니다. 이용해 주셔서 감사하였습니다. 다음에 다시 만나요 :)! ')
                        break;
                    default:
                        Warning.show(' 이메일 혹은 비밀번호가 올바르지 않습니다. 확인 후 다시 로그인 부탁드립니다.')
                        break;
                }
            } else {
                Warning.show('\'POST\', \'./userLogin\' // 서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.')
            }
        }
    };
    xhr.send(formData);
};