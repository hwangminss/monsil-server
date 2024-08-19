document.addEventListener('DOMContentLoaded', function() {
    let startY;
    let isDragging = false;
    let container = document.querySelector('.container');
    let section2 = document.querySelector('.section2');

    if (!section2) {
        console.error('Section 2 element not found');
        return;
    }

    function fadeOutAndMoveUp() {
        container.classList.add('fade-out');
        container.classList.remove('show');
    }

    function fadeInAndMoveDown() {
        container.classList.remove('fade-out');
        container.classList.add('show');
    }

    function isNearTopOfSection2() {
        let rect = section2.getBoundingClientRect();
            console.log(rect.top);

        return rect.top >= 0;
    }

    document.addEventListener('touchstart', function(event) {
        startY = event.touches[0].clientY;
    }, { passive: true });

    document.addEventListener('touchmove', function(event) {
        let currentY = event.touches[0].clientY;

        if (startY && startY - currentY > 1) {
            fadeOutAndMoveUp();
        } else if (currentY - startY > 1 && !isNearTopOfSection2()) {
            fadeInAndMoveDown();
        }
    }, { passive: true });

    document.addEventListener('touchend', function(event) {
        startY = null;
    }, { passive: true });

    document.addEventListener('mousedown', function(event) {
        startY = event.clientY;
        isDragging = true;
    });

    document.addEventListener('mousemove', function(event) {
        if (!isDragging) return;
        let currentY = event.clientY;

        if (startY && startY - currentY > 1) {
            fadeOutAndMoveUp();
        } else if (currentY - startY > 1 && !isNearTopOfSection2()) {
            fadeInAndMoveDown();
        }
    });

    document.addEventListener('mouseup', function(event) {
        isDragging = false;
        startY = null;
    });

    document.addEventListener('wheel', function(event) {
        if (event.deltaY > 1) {
            fadeOutAndMoveUp();
        } else if (event.deltaY < -1 && isNearTopOfSection2()) {
            fadeInAndMoveDown();
        }
    });
});
