/*
 * cordova is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2011, IBM Corporation
 */

/**
 * Constructor
 */
function Downloader() {
};

Downloader.prototype.downloadFile = function(fileUrl, params, win, fail) {	
	cordova.exec(win, fail, "Downloader", "downloadFile", [fileUrl, params]);
};

/**
 * Maintain API consistency with iOS
 */
Downloader.prototype.install = function(){
};

/**
 * Load Downloader
 */
if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.downloader) {
    window.plugins.downloader = new Downloader();
}