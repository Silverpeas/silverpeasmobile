var EmailPlugin = function() {
};

EmailPlugin.prototype.list = function(email, successCallback,
		failureCallback) {
	return PhoneGap.exec(successCallback, // Success callback from the plugin
	failureCallback, // Error callback from the plugin
	'EmailPlugin',
	'email', // Tell plugin, which action we want to perform
	[ email ]); // Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("EmailPlugin", new EmailPlugin());
});