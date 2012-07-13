var OrientationExifPlugin = function() {
};

OrientationExifPlugin.prototype.list = function(imagePath, successCallback,
		failureCallback) {
	return PhoneGap.exec(successCallback, // Success callback from the plugin
	failureCallback, // Error callback from the plugin
	'OrientationExifPlugin',
	'path', // Tell plugin, which action we want to perform
	[ imagePath ]); // Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("OrientationExifPlugin", new OrientationExifPlugin());
});