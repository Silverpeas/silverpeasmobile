/**
 *
 * User: svu
 * Date: 24/03/16
 */

document.addEventListener('DOMContentLoaded', function () {


    var scale = 1,
        gestureArea = document.getElementById('gesture-area'),
        scaleElement = document.getElementById('scale-element'),
        realElement = document.getElementById('real-element'),
        resetTimeout;

    /* resize the content to fit the screen */
    var scale = (screen.width  / realElement.clientWidth) * 0.95;
    scaleElement.style.webkitTransform = scaleElement.style.transform = 'scale(' + scale + ')';
    scaleElement.style.webkitTransformOrigin = scaleElement.style.transformOrigin = '0 0';

    interact(gestureArea)
        .gesturable({
            onstart: function (event) {
                clearTimeout(resetTimeout);
                scaleElement.classList.remove('reset');
            },
            onmove: function (event) {
                scale = scale * (1 + event.ds);

                scaleElement.style.webkitTransform =
                    scaleElement.style.transform =
                        'scale(' + scale + ')';

                dragMoveListener(event);
            },
            onend: function (event) {
                resetTimeout = setTimeout(reset, 1000);
                scaleElement.classList.add('reset');
            }
        })
        .draggable({onmove: dragMoveListener});

});

function reset () {
    scale = 1;
    scaleElement.style.webkitTransform =
        scaleElement.style.transform =
            'scale(1)';
}

function dragMoveListener (event) {
    var target = event.target,
    // keep the dragged position in the data-x/data-y attributes
        x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
        y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

    // translate the element
    target.style.webkitTransform =
        target.style.transform =
            'translate(' + x + 'px, ' + y + 'px)';

    // update the posiion attributes
    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
}

