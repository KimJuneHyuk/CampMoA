// // const likeBtn = window.document.getElementById('likeButton');
// // const retractBtn = window.document.getElementById('retractButton');
// // console.log(likeBtn);
// // console.log(retractBtn);
// //
// //
// // likeBtn.addEventListener('click', e => {
// //     e.preventDefault();
// //
// //     const xhr = new XMLHttpRequest();
// //     const method = likeBtn === true ? 'DELETE' : 'POST';
// //     xhr.open(method, './article-liked');
// //     xhr.onreadystatechange = () => {
// //         if (xhr.readyState === XMLHttpRequest.DONE) {
// //             if (xhr.status >= 200 && xhr.status < 300) {
// //                 const responseJson = JSON.parse(xhr.responseText);
// //                 console.log(responseJson['result']);
// //                 switch (responseJson['result']) {
// //                     case 'success':
// //                             if (likeBtn === true) {
// //                                 alert('좋아요를 클릭하셨습니다.');
// //                                 const aid = responseJson['aid'];
// //                                 window.location.href=`read?aid=${aid}`
// //                             }
// //                         break;
// //                     default:
// //                         alert('좋아요 실패!!!')
// //                         break;
// //                 }
// //             } else {
// //                 alert('좋아요 실패!!! 서버와 연결 하지 못함.')
// //             }
// //         }
// //     };
// //     xhr.send();
// // });
//
//
// const form = window.document.getElementById('form');
//
// var aid = $("#aid").val();
// var userEmail = $("#userEmail").val();
// var count = 0;
//
// $("#likeButton").click(function () {
//     count++;
//     $.ajax({
//         url: `like/${aid}/${userEmail}`,
//         type: 'get',
//         dataType: 'json',
//         success: insertLike,
//         error: function () {
//             alert("ERROR")
//         }
//
//     })
// })
//
// function insertLike() {
//     $('#likeButton').css({ "display": "none"});
//     $('#retractButton').css({'display': 'block'});
//
//     var fData = $('#form').serialize();
//     // serialize() 는 form 안에 모든 값을 전부 가져 올떄 사용한다.
//     console.log(fData);
//
//     $.ajax({
//         url: `read`,
//         type: 'get',
//         data: fData,
//         success: makeHartFull,
//         error: function (){alert('하트 채우지 못함 error')}
//     })
// }
//
// function makeHartFull() {
//     $('#fHeart').css({'display': 'none'});
//     var heart = "<span th:if='${like > 0}' th:text='${`list`}' class='likeCount' id='fHeart'>" + count + "</span>"
//     $("#heart").html(heart);
// }
const form = document.getElementById('form');
const aid = form.getElementsByTagName('input').item(0).value;
const paging = window.document.getElementById('page').value;
const userEmail = form.getElementsByTagName('input').item(2).value;
const likeCountSpan = document.getElementsByClassName('likeCount').item(0);
const heart = document.getElementsByClassName('heart').item(0);
console.log(heart)
const likeBtn = document.getElementById('likeButton');
const likeCancelBtn = document.getElementById('retractButton');
console.log(userEmail)

//처음 페이지 접속 시
get_like_data();

//좋아요 버튼 누를 시
likeBtn.onclick = () => {
    post_like_data();
}

//좋아요 취소 버튼 누를 시
likeCancelBtn.onclick = () => {
    delete_like_data();
}

function button_change(checked){
    if(checked){
        likeBtn.classList.remove('visible');
        likeCancelBtn.classList.add('visible');
        heart.classList.add('visible');
    }else{
        likeBtn.classList.add('visible');
        likeCancelBtn.classList.remove('visible');
        heart.classList.remove('visible');
    }
}


//좋아요/좋아요취소 눌렀을 때와 현재 페이지 로딩되었을 때 동작하면 됨
function get_like_data(){
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/qna/article-like?aid=${aid}`);
    xhr.send();
    xhr.onload = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.response);
                console.log(xhr.response)

                likeCountSpan.textContent = count_like(response); //현재 눌려진개수 (나 포함);
                button_change(check_liked(response));
            }
        }
    }
}

function post_like_data(){
    const xhr = new XMLHttpRequest();
    xhr.open('POST', `/qna/article-like?userEmail=${userEmail}&aid=${aid}`);
    xhr.send();
    xhr.onload = () => {
        console.log(userEmail)
        if (xhr.readyState === XMLHttpRequest.DONE) {
            count_like(userEmail)
            if (xhr.status >= 200 && xhr.status < 300) {
                console.log(userEmail)
                get_like_data();
            }
        }
    }
}

function delete_like_data(){
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', `/qna/article-like?userEmail=${userEmail}&aid=${aid}`);
    xhr.send();
    xhr.onload = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                get_like_data();
            }
        }
    }
}

function check_liked(response){
    for(value of response){
        if(value.userEmail ===  userEmail){
            return true;
        }
    }
    return false;
}

function count_like(response){
    return response.length;
}




// 댓글 달기

$('.replyForm').hide();

$(document).ready(function () {
    $('.replyBtn').each(function (index) {
        $(this).addClass('replyBtn' + index);
        $('.replyBtn' + index).click(function () {
            $('.replyForm' + index).show();
        })
    })
    $('.replyForm').each(function (index) {
        $(this).addClass('replyForm' + index);
    })
    $('.replyTextarea').each(function (index) {
        $(this).addClass('replyTextarea' + index);
    })
    $('.replySendBtn').each(function (index) {
        $(this).addClass('replySendBtn' + index);
    })
})



const commentBtn = document.getElementById("commentBtn");
commentBtn.addEventListener('click', function () {
    const articleIndex = $("#commentBNum").val();
    const content = $("#comment").val();
    const userEmail = $("#commentWriter").val();
    const page = $("#page").val();

    if(content === ""){
        alert("댓글 내용을 입력하세요!")
        return false;
    }

    $.ajax({
        url: "/qna/insertComment",
        method: "GET",
        data: {"content": content, "userEmail": userEmail, "articleIndex": articleIndex},
        success: function (responseJson) {
            responseJson = JSON.parse(responseJson)
            if (responseJson['result'] === 1) {
                alert("댓글이 게시 되었습니다.")
                window.location.href=`/qna/read?page=${page}&aid=${articleIndex}`
            } else {
                alert("댓글작성에 실패하였습니다. 잠시후 다시 시도해 주세요.");
                window.location.href=`/qna/read?page=${page}&aid=${articleIndex}`
            }
        }
    })
})



