HTMLInputElement.prototype.focusAndSelect = function () {
    this.focus();
    this.select();
}
// 프로토 타입...........익명함수 생성.

const _writeButton = window.document.getElementById('_writeButton');
const _writeMenu = window.document.getElementById('_writeMenu');
_writeButton?.addEventListener('click', () => {
    if (_writeMenu?.classList.contains('visible')) {
        _writeMenu?.classList.remove('visible')
    } else {
        _writeMenu?.classList.add('visible');
    }
});

_writeMenu?.addEventListener('mouseleave', () => {
    _writeMenu?.classList.remove('visible');
})



$('.multiple-items').slick({
    infinite: true,
    slidesToShow: 4,
    slidesToScroll: 1
});


$(".slick-next").text(">>");
$(".slick-prev").text("<<");
