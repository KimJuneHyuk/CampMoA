const Btn = document.getElementById("mySwitch");
const pw1 = document.querySelector("#NewPw1");
const pw2 = document.querySelector("#NewPw2");
const pw3 = document.querySelector("#NewPw3");
const form = window.document.getElementById('form');
const recoverBtn = window.document.getElementById("recoverBtn");

const Warning = {
    getElement: () => document.getElementById('warning'),
    hide: () => Warning.getElement().classList.remove('visible'),
    show: (text) => {
        Warning.getElement().innerText = text;
        Warning.getElement().classList.add('visible');
    }
};
const oldPwCheckBtn = window.document.getElementById('oldPwCheckBtn');
console.log(oldPwCheckBtn)


Btn.addEventListener('input', function () {

    // e.preventDefault();

    if (Btn.checked) {
        pw1.removeAttribute('disabled');

    } else {
        pw1.setAttribute('disabled', 'disabled');
    }

    checkPassword();
});


recoverBtn.addEventListener('click', e => {
    e.preventDefault();
    // alert("클릭")

    const email = form['email'].value;
    const password = form['NewPw2'].value;
    const newPwCheck = form['NewPw3'].value;

    if (password === '') {
        Warning.show('새로운 비밀번호를 입력해 주세요.');
        form['NewPw2'].focus();
        form['NewPw2'].select();
        return false;
    }

    if (newPwCheck === '') {
        Warning.show('비밀번호 설정 확인 을 입력해 주세요.');
        form['NewPw3'].focus();
        form['NewPw3'].select();
        return false;
    }

    if (password !== newPwCheck) {
        Warning.show('비밀번호가 다릅니다. 다시 입력해주세요.')
        return false;
    }

    if (password === newPwCheck) {
        $.ajax({
            url: '/member/userRecoverPassword',
            method: 'patch',
            data:{ "email": email , "password": password },
            success: function (responseJson) {

                responseJson = JSON.parse(responseJson);
                console.log(responseJson);

                if (responseJson['result'] === 'success') {
                    alert('비밀번호를 변경하였습니다.');
                    window.location.href = "/";

                } else {
                    alert("비밀번호 변경에 실패하였습니다.");
                }
            },
            error: function () {
                alert("서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.")
            }
        });
    }
})




function checkPassword() {
    // e.preventDefault()

    if (pw1.value === '') {
        Warning.show('현재 비밀번호를 입력해 주세요.');
        pw1.focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', form['email'].value);
    formData.append('password', form['password'].value);
    xhr.open('POST', 'userPasswordCheck');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        Warning.hide();
                        pw1.setAttribute('disabled', 'disabled');
                        pw2.removeAttribute('disabled');
                        pw3.removeAttribute('disabled');
                        if(!Btn.checked){
                            pw2.setAttribute('disabled','disabled');
                            pw3.setAttribute('disabled','disabled');
                        }
                        break;
                    case 'suspended':
                        Warning.show('이용이 정지된 회원입니다.')
                        break;
                    case 'resigned':
                        Warning.show('해당 회원은 탈퇴를 진행한 회원 입니다. 이용해 주셔서 감사하였습니다. 다음에 다시 만나요 :)! ')
                        break;
                    default:
                        Warning.show('비밀번호가 일치하지 않거나 혹은 비밀번호가 올바르지 않습니다. 확인 후 다시 로그인 부탁드립니다.')
                        break;
                }
            } else {
                Warning.show('\'GET\', \'./userPasswordCheck\' // 서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.')
            }
        }
    };
    xhr.send(formData);
};
