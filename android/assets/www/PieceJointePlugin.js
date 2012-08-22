var PieceJointePlugin = function() {
};

PieceJointePlugin.prototype.list = function(filePath, successCallback,
		failureCallback) {
	return PhoneGap.exec(successCallback, // Success callback from the plugin
	failureCallback, // Error callback from the plugin
	'PieceJointePlugin',
	'path', // Tell plugin, which action we want to perform
	[ filePath ]); // Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("PieceJointePlugin", new PieceJointePlugin());
});