var OrientationExifPlugin = function() {
};

OrientationExifPlugin.prototype.orientation = function(path, successCallback,
		failureCallback) {
	return cordova.exec(successCallback, // Success callback from the plugin
	failureCallback, // Error callback from the plugin
	'OrientationExifPlugin',
	'path', // Tell plugin, which action we want to perform
	[ path ]); // Passing list of args to the plugin
};

cordova.addConstructor(function() {
	cordova.addPlugin("OrientationExifPlugin", new OrientationExifPlugin());
});