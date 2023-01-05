const Btn = document.getElementById("mySwitch");
const pw1 = document.querySelector("#NewPw1");
const pw2 = document.querySelector("#NewPw2");
const pw3 = document.querySelector("#NewPw3");

Btn.addEventListener('input', function(){
    if(Btn.checked){
        pw1.removeAttribute('disabled');
        pw2.removeAttribute('disabled');
        pw3.removeAttribute('disabled');
    } else{
        pw1.setAttribute('disabled','disabled');
        pw2.setAttribute('disabled','disabled');
        pw3.setAttribute('disabled','disabled');
    }


});