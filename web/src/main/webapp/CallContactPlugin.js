var CallContactPlugin = function() {
};

CallContactPlugin.prototype.list = function(phoneNumber, successCallback,
		failureCallback) {
	return PhoneGap.exec(successCallback, // Success callback from the plugin
	failureCallback, // Error callback from the plugin
	'CallContactPlugin',
	'phone', // Tell plugin, which action we want to perform
	[ phoneNumber ]); // Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("CallContactPlugin", new CallContactPlugin());
});