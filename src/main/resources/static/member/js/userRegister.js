const Form = document.getElementById('registerForm');
const Warning = {
    getElement: () => document.getElementById('warning'),
    hide: () => Warning.getElement().classList.remove('visible'),
    show: (text) => {
        Warning.getElement().innerText = text;
        Warning.getElement().classList.add('visible');
    }
};

console.log(Form)
console.log(Warning.getElement())

const functions = {
    checkContactAuthCode: () => {
        if (Form['contactAuthCode'].value === '') {
            Warning.show('인증번호를 입력해 주세요.');
            Form['contactAuthCode'].focus();
            return;
        }
        if (!new RegExp('^(\\d{6})$').test(Form['contactAuthCode'].value)) {
            Warning.show('올바른 인증번호를 입력해 주세요.');
            Form['contactAuthCode'].focusAndSelect();
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('contact', Form['contact'].value);
        formData.append('code', Form['contactAuthCode'].value);
        formData.append('salt', Form['contactAuthSalt'].value);
        xhr.open('POST', './userRegisterAuth');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'expired':
                            Warning.show('입력한 인증번호가 만료되었습니다. 인증번호를 다시 요청하여 인증해 주세요.');
                            Form['contact'].removeAttribute('disabled');
                            Form['contactAuthRequestButton'].removeAttribute('disabled');
                            Form['contactAuthCode'].value = '';
                            Form['contactAuthCode'].setAttribute('disabled', 'disabled');
                            Form['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            Form['contactAuthSalt'].value = '';
                            Form['contact'].focusAndSelect();
                            break;
                        case 'success':
                            Form['contactAuthCode'].setAttribute('disabled', 'disabled');
                            Form['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            Warning.show('연락처가 성공적으로 인증되었습니다.');
                            break;
                        default:
                            Form['contactAuthCode'].focusAndSelect();
                            Warning.show('입력한 인증번호가 올바르지 않습니다.');
                    }
                } else {
                    Warning.show('checkContactAuthCode :: 서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    Form['contactAuthCode'].focusAndSelect();
                }
            }
        };
        xhr.send(formData);
    },
    requestContactAuthCode: () => {
        if (Form['contact'].value === '') {
            Warning.show('연락처를 입력해 주세요.');
            Form['contact'].focus();
            return;
        }
        if (!new RegExp('^(\\d{8,12})$').test(Form['contact'].value)) {
            Warning.show('올바른 연락처를 입력해 주세요.');
            Form['contact'].focusAndSelect();
            return false;
        }

        const xhr = new XMLHttpRequest();
        xhr.open('GET', `./userRegisterAuth?contact=${Form['contact'].value}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'duplicate':
                            Warning.show('해당 연락처는 이미 사용 중입니다.');
                            Form['contact'].focusAndSelect();
                            break;
                        case 'success':
                            Warning.show('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다. 5분 내로 문자로 전송된 인증번호를 확인해 주세요.');
                            Form['contactAuthSalt'].value = responseJson['salt'];
                            Form['contact'].setAttribute('disabled', 'disabled');
                            Form['contactAuthRequestButton'].setAttribute('disabled', 'disabled');
                            Form['contactAuthCode'].removeAttribute('disabled');
                            Form['contactAuthCheckButton'].removeAttribute('disabled');
                            Form['contactAuthCode'].focusAndSelect();
                            break;
                        default:
                            Warning.show('알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                            Form['contact'].focusAndSelect();
                    }
                } else {
                    Warning.show('requestContactAuthCode :: 서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    Form['contact'].focusAndSelect();
                }
            }
        };
        xhr.send();
    }
};

let emailChecked = false;

window.document.body.querySelectorAll('[data-func]').forEach(x => {
    x.addEventListener('click', e => {
        if (typeof (functions[x.dataset.func]) === 'function') {
            functions[x.dataset.func]({
                element: x,
                event: e
            });
        }
    });
});

Form.onsubmit = e => {
    e.preventDefault();
    Warning.hide();

    if (Form['email'].value === '') {
        Warning.show('이메일 주소 입력해주세요');
        Form['email'].focus();
        return false;
    }
    if (!new RegExp('^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$').test(Form['email'].value)) {
        Warning.show('올바른 이메일 주소를 입력해 주세요.');
        Form['email'].focus();
        Form['email'].select();
        return false;
    }


    if (Form['password'].value === '') {
        Warning.show('비밀번호를 입력해 주세요.');
        Form['password'].focus();
        return false;
    }
    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{8,50})$').test(Form['password'].value)) {
        Warning.show('올바른 비밀번호를 입력해 주세요.');
        Form['password'].focus();
        Form['password'].select();
        return false;
    }
    if (Form['passwordCheck'].value === '') {
        Warning.show('비밀번호 재입력해 주세요.');
        Form['passwordCheck'].focus();
        return false;
    }
    if (Form['password'].value !== Form['passwordCheck'].value) {
        Warning.show('입력한 비밀번호가 서로 일치하지 않습니다.');
        Form['passwordCheck'].focus();
        return false;
    }


    if (Form['name'].value === '') {
        Warning.show('이름을 입력해 주세요.');
        Form['name'].focus();
        return false;
    }
    if (!new RegExp('^([가-힣]{2,5})$').test(Form['name'].value)) {
        Warning.show('올바른 이름을 입력해 주세요.');
        Form['name'].focusAndSelect();
        return false;
    }



    if (Form['contact'].value === '') {
        Warning.show('연락처를 입력해 주세요.');
        Form['contact'].focus();
        return false;
    }
    if (!new RegExp('^(\\d{8,12})$').test(Form['contact'].value)) {
        Warning.show('올바른 연락처를 입력해 주세요.');
        Form['contact'].focusAndSelect();
        return false;
    }


    if (!Form['contactAuthRequestButton'].disabled) {
        Warning.show('연락처를 인증해 주세요.');
        Form['contact'].focusAndSelect();
        return false;
    }
    if (!Form['contactAuthCheckButton'].disabled) {
        Warning.show('연락처 인증을 마무리해 주세요.');
        Form['contactAuthCode'].focusAndSelect();
        return false;
    }
    if (Form['contactAuthSalt'].value === '') {
        Warning.show('연락처 인증이 정상적으로 완료되지 않았습니다.');
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', Form['email'].value);
    formData.append('password', Form['password'].value);
    formData.append('name', Form['name'].value);
    formData.append('contactCountryValue', Form['contactCountryValue'].value);
    formData.append('contact', Form['contact'].value);
    formData.append('code', Form['contactAuthCode'].value);
    formData.append('salt', Form['contactAuthSalt'].value);

    xhr.open('POST', './userRegister');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson['result'] + "post :: userRegister")
                switch (responseJson['result']) {
                    case 'success':
                        window.location.href ='./userRegisterDone';
                        break;
                    default:
                        Warning.show('post :: ./userRegister // 알수 없는 이유로 회원가입을 하지 못하였습니다.')
                }
            } else {
                Warning.show('post :: ./userRegister // 서버와 통신하지 못하였습니다.');
            }
        }
    };
    xhr.send(formData);
};

