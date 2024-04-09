function initCropper(image, ratio) {
    var cropper = new Cropper(image, {
        aspectRatio: ratio,
    });
    window.cropper = cropper;
}

function getCanvasData() {
    return window.cropper.getCroppedCanvas().toDataURL();

}

function destroyCropper() {
    if (window.cropper) window.cropper.destroy();
}


