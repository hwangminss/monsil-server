
var _SPINNER = null;

function setStartSpinner() {

    var opts = {
        lines: 8, // 그릴 선의 수 [20=원형 / 10=막대] [The number of lines to draw]
        length: 0, // 각 줄의 길이 [0=원형 / 10=막대] [The length of each line]
        width: 28, // 선 두께 [The line thickness]
        radius: 30, // 내부 원의 반지름 [The radius of the inner circle]
        scale: 0.55, // 스피너의 전체 크기 지정 [Scales overall size of the spinner]
        corners: 1, // 모서리 라운드 [Corner roundness (0..1)]
        color: '#61c5fa', // 로딩 CSS 색상 [CSS color or array of colors]
        fadeColor: 'transparent', // 로딩 CSS 색상 [CSS color or array of colors]
        opacity: 0.05, // 선 불투명도 [Opacity of the lines]
        rotate: 0, // 회전 오프셋 각도 [The rotation offset]
        direction: 1, // 회전 방향 시계 방향, 반시계 방향 [1: clockwise, -1: counterclockwise]
        speed: 1, // 회전 속도 [Rounds per second]
        trail: 74, // 꼬리 잔광 비율 [Afterglow percentage]
        fps: 20, // 초당 프레임 수 [Frames per second when using setTimeout() as a fallback in IE 9]
        zIndex: 2e9 // 인덱스 설정 [The z-index (defaults to 2000000000)]

    };

    var target = document.getElementById('spinners');

    if (_SPINNER == null) {
        _SPINNER = new Spinner(opts).spin(target);
    }
}
function setStopSpinner() {

    if (_SPINNER != null) {
        _SPINNER.stop();
        _SPINNER = null;
    }
}
