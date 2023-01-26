const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);
const coverImage = window.document.getElementById('coverImage');
const title = window.document.getElementById('title');
const region = window.document.getElementById('region');
const capacity = window.document.getElementById('capacity');
const dateFrom = window.document.getElementById('dateFrom');
const dateTo = window.document.getElementById('dateTo');
const content = window.document.getElementById('content');
const createdAt = window.document.getElementById('createdAt');
const name = window.document.getElementById('name');

const requestButton = window.document.getElementById('requestButton');
const modifyButton = window.document.getElementById('modifyButton');
const deleteButton = window.document.getElementById('deleteButton');
const retractButton = window.document.getElementById('retractButton');

console.log(requestButton)

const checkRequest = () => {
    // cover.show('ㄱㄷ');
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `../request/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson['result'])
                if (responseJson['result'] === true) {
                    //    요청한적 있음.
                    retractButton.classList.add('visible');
                } else {
                    //    없음
                    requestButton.classList.add('visible');
                }
            } else {
                alert('일부 정보를 불러오지 못하였습니다.');
            }
        }
    };
    xhr.send();
};

// cover.show('요청한 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.');
const xhr = new XMLHttpRequest();
// xhr.open('POST', window.location.href);
xhr.open('POST', window.location.href); // /accompany/read/??
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        // cover.hide();
        if (xhr.status >= 200 && xhr.status < 300) {

            const responseJson = JSON.parse(xhr.responseText);
            const createdAtObj = new Date(responseJson['createdAt']);
            const dateFromObj = new Date(responseJson['dateFrom']);
            const dateToObj = new Date(responseJson['dateTo']);

            window.document.title = `${responseJson['title']} :: 드립소다`;
            coverImage.setAttribute('src', `../cover-image/${id}`);
            title.innerText = responseJson['title'];
            region.innerText = responseJson['regionValue'];
            capacity.innerText = `${responseJson['capacity']}명`;
            dateFrom.innerText = `${dateFromObj.getFullYear()}-${dateFromObj.getMonth() + 1}-${dateFromObj.getDate()}`;
            dateTo.innerText = `${dateToObj.getFullYear()}-${dateToObj.getMonth() + 1}-${dateToObj.getDate()}`;
            content.innerHTML = responseJson['content'];
            name.innerText = responseJson['userName'];
            //====================================================
            console.log("mine 값 : " + responseJson['mine']);
            //===================================================
            if ((responseJson['mine'] ?? false) === true) {
                modifyButton.classList.add('visible');
                deleteButton.classList.add('visible');
            }
            createdAt.innerText = `${createdAtObj.getFullYear()}-${createdAtObj.getMonth() + 1}-${createdAtObj.getDate()} ${createdAtObj.getHours()}:${createdAtObj.getMinutes()}`;

            checkRequest();
        } else if (xhr.status === 404) {
            alert('존재하지 않는 동행 게시글입니다.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        }
    }
};
xhr.send();

deleteButton.addEventListener('click', () => {
    if (!confirm('정말로 삭제 하시겠습니까?')) {
        return;
    }
    // cover.show('동행을 삭제하고 있습니다.\n\n잠시만 기다려 주세요.')

    const xhr = new XMLHttpRequest();

    xhr.open('DELETE', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('동행을 성공적으로 삭제하였습니다.');
                        window.location.href = '../';
                        break;
                    case 'k':
                        alert('당신이 작성한 게시글이 아닙니다. 삭제 불가능 합니다.');
                        break;
                    default:
                        alert('알 수 없는 이유로 동행을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});


modifyButton.addEventListener('click', () => {
    window.location.href = `../modify/${id}`;
})

requestButton.addEventListener('click', () => {
    console.log(requestButton.dataset.signed)

    if (requestButton.dataset.signed === "false") {
        console.log("test");
        window.location.href = "../../member/userLogin";
        return false;
    }
    const xhr = new XMLHttpRequest();
    xhr.open('POST', `../request/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 동행 정보입니다.');
                        window.location.href = '../'
                        break;
                    case 'not_signed' :
                        alert('로그인 정보가 유효하지 않습니다.');
                        break;
                    case 'yourself' :
                        alert('본인이 작성한 게시글의 동행신청은 불가능 합니다.');
                        break;
                    case 'success' :
                        alert('동행 신청에 성공하였습니다.');
                        window.location.reload();
                        break;
                    default:
                        alert('알 수 없는 이유로 동행 신청에 실패하였습니다. 잠시후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});











