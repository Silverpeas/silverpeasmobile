var httpServerBase;
var path = new Array();
var rootPublicationsCount = 0;
var highlightedDocumentId = null;
var appletTopicId = null;

var treeView;
var root;
var currentNodeId;
var currentNodeIndex = 0;
var currentTextNode = null;

var PROFILE_ADMIN = 3;
var PROFILE_WRITER = 2;
var PROFILE_READER = 1;

var context = null;
var componentLabel = null;
var componentId = null;
var language = null;
var userId = null;
var userProfile = "reader";
var spaceId = null;


var LABEL_BASKET = "Corbeille";

var topicWindow = window;

function initTree(id) {
	//create a new tree:
    treeView = new YAHOO.widget.TreeView("treeView");

    //turn dynamic loading on for entire tree:
    treeView.setDynamicLoad(loadNodeData, 0);
    //treeView.singleNodeHighlight = true;

    root = new YAHOO.widget.TextNode({"id": "0", "role": /*userProfileName*/"reader"}, treeView.getRoot(), true);
	root.labelElId = "0";
	root.label = componentLabel;
    root.href = "javascript:displayTopicContent(0)";

	//render tree with these toplevel nodes; all descendants of these nodes
	//will be generated as needed by the dynamic loader.
	treeView.render();

	setCurrentNodeId(id);

	//let the time to tree to be loaded !
	setTimeout("displayTopicContent(" + id + ")", 500);

	treeView.subscribe("expandComplete", function(node) {
		//highlight node
		if (node.data.id == id) {
			//currentNodeIndex = node.index;
			$("#ygtvcontentel" + currentNodeIndex).css({"font-weight": "bold"});
		}
	});

	treeView.subscribe("clickEvent", function(args) {
		$("#ygtvcontentel" + currentNodeIndex).css({"font-weight": "normal"});
		setCurrentNodeId(args.node.data.id);
		currentNodeIndex = args.node.index;
		$("#ygtvcontentel" + currentNodeIndex).css({"font-weight": "bold"});
		displayTopicContent(getCurrentNodeId());
	});
}

function getCurrentNodeId() {
	return currentNodeId;
}

function setCurrentNodeId(id) {
	currentNodeId = id;
}

function loadNodeData(node, fnLoadComplete) {
    var url = getJsonUrl("topics", node.labelElId);

    var callback = {

        // if our XHR call is successful, we want to make use of the returned data and create
        // child nodes.
        success: function(response) {
            YAHOO.log("XHR transaction was successful.", "info", "example");

            var messages = [];

            // Use the JSON Utility to parse the data returned from the server
            try {
                messages = YAHOO.lang.JSON.parse(response.responseText);
            } catch(x) {
                alert("JSON Parse failed!");
                return;
            }

            //var tempNode;
            var userProfile = 1;
            var isBasket = false;
            var nbItemsInBasket = 0;
            // The returned data was parsed into an array of objects.
            var nbPublisOnRoot = rootPublicationsCount;
            for (var i = 0, len = messages.length; i < len; ++i) {
                var m = messages[i];
                if (m.id == "0") {
	                if (m.nbObjects != -1) {
                		root.label = root.label + getDocumentsCount(m.nbObjects);
                		nbPublisOnRoot += m.nbObjects;
	                }
                } else if (m.id == "1") {
                    isBasket = true;
                    nbItemsInBasket = m.nbObjects;
                } else if (m.id == "2" && userProfile != PROFILE_ADMIN) {
                	// nothing
                } else {
                    var tempNode = new YAHOO.widget.TextNode(m, node, isNodeInPath(m.id));
                    tempNode.labelElId = m.id;
                    tempNode.label = m.name + getDocumentsCount(m.nbObjects);
                    tempNode.title = m.description;
                    if (node.data.role == "admin") {
                        //node's label is only editable if user is admin on parent node
                    	tempNode.editable = true;
                    }
                }
                if ((m.nbObjects != -1) && (m.level == "2")
    	                && ((m.id != "1") || (userProfile == PROFILE_ADMIN))) {
                	nbPublisOnRoot += m.nbObjects;
                }
            }
            if (isBasket) {
	            if (userProfile == PROFILE_ADMIN || userProfile == PROFILE_WRITER) {
	            	//add basket
		   			var basketNode = new YAHOO.widget.TextNode({"id": "1"}, node, false);
		   			basketNode.labelElId = "basket";
		   			basketNode.label = LABEL_BASKET;
		   			if (nbItemsInBasket != -1) {
		   				basketNode.label = basketNode.label + getDocumentsCount(nbItemsInBasket);
		   			}
		   			basketNode.href = "javascript:displayTopicContent(1)";
		   			basketNode.isLeaf = true;
		   			basketNode.hasIcon = true;
					basketNode.labelStyle = "icon-basket";
                }
            }

            if (rootPublicationsCount != -1 && nbPublisOnRoot > 0) {
            	root.label = root.label + getDocumentsCount(nbPublisOnRoot);
            	// $("#" + root.labelElId).html(componentLabel + getDocumentsCount(nbPublisOnRoot));
            	// When the treeview is expanded to several levels in one time (special redirection
            	// to a specific document from the dashboard for instance), function loadNodeData is
            	// called many times. Since only the first call gives a correct root's publications
            	// number, the following line disables the next update attempts.
            	rootPublicationsCount = -1;
            	//root.refresh();
            }
            
            // When we're done creating child nodes, we execute the node's loadComplete callback
            // method which comes in via the argument in the response object (we could also
            // access it at node.loadComplete, if necessary):
            response.argument.fnLoadComplete();
        },

        // If our XHR call is not successful, we want to fire the TreeView callback and let the
        // Tree proceed with its business.
        failure: function(response) {
            YAHOO.log("Failed to process XHR transaction.", "info", "example");
            response.argument.fnLoadComplete();
        },

        // Our handlers for the XHR response will need the same argument information we got to
        // loadNodeData, so we'll pass those along:
        
        argument: {
            "node": node,
            "fnLoadComplete": fnLoadComplete
        },

        // timeout -- if more than 7 seconds go by, we'll abort the transaction and assume there
        // are no children:
        timeout: 7000
    };

  	// With our callback object ready, it's now time to make our XHR call using Connection
  	// Manager's asyncRequest method:
    YAHOO.util.Connect.asyncRequest("GET", url, callback);
}

function isNodeInPath(id) {
	var i;
	for (i = 0; i < path.length; i++) {
		if (id == path[i]) {
			return true;
		}
	}
	return false;
}

function getDocumentsCount(count) {
	if (count != -1) {
		return " <span class=\"documentsCount\">(" + count + ")</span>";
	} else {
		return "";
	}
}

var displayTopicContentId = null;

function displayTopicContent(id) {
	if (id != displayTopicContentId) {
		displayTopicContentId = id;
		if (id != "1") {
			var node = treeView.getNodeByProperty("labelElId", id);
			// highlight current topic
			$("#ygtvcontentel" + currentNodeIndex).css({"font-weight": "normal"});
			try {
				setCurrentNodeId(node.data.id);
				currentNodeIndex = node.index;
				$("#ygtvcontentel" + currentNodeIndex).css({"font-weight": "bold"});
			} catch(e) {
				//TO FIX
			}
		} else {
			$("#menutoggle").css({"display": "none"}); //hide operations
		}
		//checkDnD(id);
		displayPublications(id);
		//displayPath(id);
	}
}

function checkDnD(id) {
	if ((id != "1") && (userProfile == PROFILE_ADMIN || userProfile == PROFILE_WRITER)) {
		$("#DnD").css({"display": "block"});
		if (dNdLoaded) {
			if (appletTopicId != null && appletTopicId != id) {
				// Applet previously loaded with an other topic id. Reset applet is required.
				dNdLoaded = false;
				hideDragDrop();
				//$("#DragAndDrop").empty();
				document.getElementById("DragAndDrop").innerHTML = "";
			}
		}
	} else {
		$("#DnD").css({"display": "none"});
	}
}

function displayPath(id) {
	//prepare URL for XHR request:
	var url = getJsonUrl("path", id);
	
    //prepare our callback object
    var callback = {

        //if our XHR call is successful, we want to make use
        //of the returned data and create child nodes.
        success: function(response) {
            var messages = [];
            // Use the JSON Utility to parse the data returned from the server
            try {
                messages = YAHOO.lang.JSON.parse(response.responseText);
            } catch(e) {
                alert("JSON Parse failed!");
                return;
            }

            //remove topic breadcrumb
            removeBreadCrumbElements();

            // The returned data was parsed into an array of objects.
            for (var i = (messages.length - 1); i >= 0; i--) {
                var m = messages[i];
                if (m.id != 0) {
                	addBreadCrumbElement("javascript:goToTopic(" + m.id + ")", m.name);
                }
            }
        },

        //timeout -- if more than 7 seconds go by, we'll abort
        //the transaction and assume there are no children:
        timeout: 7000
    };

    //With our callback object ready, it's now time to
    //make our XHR call using Connection Manager's
    //asyncRequest method:
    YAHOO.util.Connect.asyncRequest("GET", url, callback);
}

function newFile() {
}

function copyTopic() {
}

function cutTopic() {
}

function pasteFromTree() {
}

function onTriggerContextMenu(event) {
    var target = this.contextEventTarget;

    /*
         Get the TextNode instance that that triggered the
         display of the ContextMenu instance.
    */
    currentTextNode = treeView.getNodeByElement(target);
    if (!currentTextNode) {
        // Cancel the display of the ContextMenu instance.
        this.cancel();
    }

    if (currentTextNode) {
		if (currentTextNode.labelElId == "0" || currentTextNode.labelElId == "basket") {
			contextMenu.cfg.setProperty("visible", false);
		} else {
		    //get profile to display more or less context actions
		    $.getJSON(
	    		getJsonUrl("topic", currentTextNode.labelElId),
				function(data) {
					try {
						var profile = data[0].role;
						var parentProfile =  currentTextNode.parent.data.role;
						if (profile == "admin") {
							//all actions are enabled
							contextMenu.getItem(0).cfg.setProperty("disabled", false);
							/*contextMenu.getItem(1).cfg.setProperty("disabled", false);
							contextMenu.getItem(2).cfg.setProperty("disabled", false);*/
							
							contextMenu.getItem(0,1).cfg.setProperty("disabled", false);
							/*contextMenu.getItem(1,1).cfg.setProperty("disabled", false);
							contextMenu.getItem(2,1).cfg.setProperty("disabled", false);*/
						} else if (profile == "reader") {
							contextMenu.cfg.setProperty("visible", false);
						} else {
							contextMenu.getItem(0,1).cfg.setProperty("disabled", true);
							/*contextMenu.getItem(1,1).cfg.setProperty("disabled", true);
							contextMenu.getItem(2,1).cfg.setProperty("disabled", true);*/

							contextMenu.getItem(0,2).cfg.setProperty("disabled", true);
							/*contextMenu.getItem(1,2).cfg.setProperty("disabled", true);*/
						}
					} catch(e) {
						//do nothing
						//alert(e);
					}
				});
		}
    }
}

function displayPublications(id) {
	$.get(
		context + "/index.html",
		{
			action: "kmelia",
			subAction: "publications",
			id: id,
			spaceId: spaceId,
			userId: userId,
			userProfile: userProfile,
			componentId: componentId,
			ieFix: new Date().getTime()
		},
		function(data) {
			$("#pubList").html(data);
		},
		"html"
	);
}

function newTopic() {
	openTopic(currentTextNode.labelElId, OPERATION_NEW_TOPIC);
}

function editTopic() {
	openTopic(currentTextNode.labelElId, OPERATION_EDIT_TOPIC);
}

function openTopic(id, operation) {
	hideDragDrop();
	$("#jWinTopic").jqmShow();
	var parameters = new Array();
	parameters.push("id", id);
	$("#jWinTopicContent").load(getAjaxUrl(ACTION_DOCUMENTS, operation, parameters));
	$("#jWinTopicContent").show();
	var left = ($(document).width() - $("#jWinTopic").width()) / 3;
	if (left < 0) {
		left = 10;
	}
	var cssLeft = "" + left + "px";
    $("#jWinTopic").css({
    	"left": cssLeft,
    	"max-height": $(document).height() - 200,
    	"overflow-y": "auto"
    });
}

function removeTopic() {
	var nodeId = currentTextNode.labelElId;
	if (window.confirm(LABEL_CONFIRM_TOPIC_DELETION + " '" + currentTextNode.data.name + "' ?")) {
		$.get(
			context + "/RofficelinkAjax",
			{
				actionId: ACTION_DOCUMENTS,
				operation: OPERATION_REMOVE_TOPIC,
				id: nodeId,
				componentId: componentId,
				ieFix: new Date().getTime()
			},
			function(data) {
				if (data == "ok") {
					treeView.removeNode(currentTextNode);
	                treeView.draw();
				} else {
					alert(data);
				}
			}
		);
	}
}

function closeTopic() {
	$("#jWinTopic").jqmHide();
}

function validateTopic() {
	var form = document.forms["officeLinkForm"];
	$.get(
		context + "/RofficelinkAjax",
		{
			actionId: ACTION_DOCUMENTS,
			operation: OPERATION_VALIDATE_TOPIC,
			parentTopicId: form.elements["parentTopicId"].value,
			topicId: form.elements["topicId"].value,
			topicName: form.elements["topicName"].value,
			componentId: componentId,
			ieFix: new Date().getTime()
		},
		function(data) {
			$("#jWinTopicContent").html(data);
		},
		"html"
	);
}

function goToTopic(id) {
	document.forms["officeLinkForm"].elements["topicIdToGo"].value = id;
	callOperation(ACTION_DOCUMENTS, OPERATION_GO_TO_TOPIC);
}

function flushBasket() {
	if (window.confirm(LABEL_CONFIRM_BASKET_FLUSH)) {
		$.get(
			context + "/RofficelinkAjax",
			{
				actionId: ACTION_DOCUMENTS,
				operation: OPERATION_FLUSH_BASKET,
				componentId: componentId,
				ieFix: new Date().getTime()
			},
			function(data) {
				if (data == "ok") {
					goToTopic(0);
				} else {
					alert(data);
				}
			}
		);
	}
}

function openDocument(id) {
	hideDragDrop();
	$("#jWinDocument").jqmShow();
	var parameters = new Array();
	parameters.push("id", id);
	$("#jWinDocumentContent").load(getAjaxUrl(ACTION_DOCUMENTS, OPERATION_EDIT_DOCUMENT, parameters));
	$("#jWinDocumentContent").show();
	var left = ($(document).width() - $("#jWinDocument").width()) / 3;
	if (left < 0) {
		left = 10;
	}
	var cssLeft = "" + left + "px";
    $("#jWinDocument").css({
    	"left": cssLeft,
    	"max-height": $(document).height()-200,
    	"overflow-y": "auto"
    });
}

function editDocument(id) {
	openDocument(id);
}

function removeDocument(id) {
	if (window.confirm(LABEL_CONFIRM_DOCUMENT_DELETION)) {
		$.get(
			context + "/RofficelinkAjax",
			{
				actionId: ACTION_DOCUMENTS,
				operation: OPERATION_REMOVE_DOCUMENT,
				topicId: currentNodeId,
				id: id,
				componentId: componentId,
				ieFix: new Date().getTime()
			},
			function(data) {
				if (data == "ok") {
					goToTopic(currentNodeId);
				} else {
					alert(data);
				}
			}
		);
	}
}

function closeDocument() {
	$("#jWinDocument").jqmHide();
}

function validateDocument() {
	var form = document.forms["officeLinkForm"];
	var id = form.elements["publicationId"].value;
	$.get(
		context + "/RofficelinkAjax",
		{
			actionId: ACTION_DOCUMENTS,
			operation: OPERATION_VALIDATE_DOCUMENT,
			id: id,
			name: form.elements["name"].value,
			description: form.elements["description"].value,
			componentId: componentId,
			ieFix: new Date().getTime()
		},
		function(data) {
			//$("#jWinDocumentContent").html(data);
			closeDocument();
			$("#publication" + id).html(data);
		},
		"html"
	);
}


var dNdVisible = false;
var dNdLoaded = false;

function showHideDragDrop(altMessage, maxUpload, expandLabel, collapseLabel) {
	var message = httpServerBase + context + "/officelink/upload/dropMode_" + language + ".html";
	var actionDND = document.getElementById("dNdActionLabel");
	if (dNdVisible) {
		//hide applet
		hideOfficeLinkApplet();
		
		//change link's label
		actionDND.innerHTML = expandLabel;
	} else {
		actionDND.innerHTML = collapseLabel;
		if (!dNdLoaded) {
			var url = httpServerBase + context + "/RImportDragAndDrop/jsp/Drop"
				+ "?UserId=" + userId
				+ "&ComponentId=" + componentId
				+ "&TopicId=" + getCurrentNodeId()
				+ "&SessionId=" + sessionId;
			if (userProfile == PROFILE_ADMIN || userProfile == PROFILE_WRITER) {
				url += "&IgnoreFolders=1";
			}
			try {
				appletTopicId = getCurrentNodeId();
				loadApplet("DragAndDrop", url, message, maxUpload, context, altMessage);
			} catch (e) {
				alert(e);
			}
			dNdLoaded = true;
		}
		showOfficeLinkApplet();
	}
	dNdVisible = !dNdVisible;
}

function hideDragDrop() {
	if (dNdVisible) {
		hideOfficeLinkApplet();
		document.getElementById("dNdActionLabel").innerHTML = LABEL_DROP_DOCUMENT;
		dNdVisible = false;
	}
}

function showOfficeLinkApplet() {
	try {
		document.getElementById("DropZone").style.display = "";
		var dndApplet = document.getElementById("appletDragAndDrop");
		if (dndApplet.getAttribute("border") == null) {
			dndApplet.setAttribute("height", "55");
			dndApplet.setAttribute("border", "1");
			addAppletParam("bgcolor_r", "209");
			addAppletParam("bgcolor_g", "209");
			addAppletParam("bgcolor_b", "209");
		}
		dndApplet.style.display = "";
	} catch(e) {
		//alert(e);
	}
}

function addAppletParam(name, value) {
  var param = document.createElement("param");
  param.setAttribute("name", name);
  param.setAttribute("value", value);
  document.getElementById("appletDragAndDrop").appendChild(param);
}

function hideOfficeLinkApplet() {
	try {
		document.getElementById("appletDragAndDrop").style.display = "none";
		document.getElementById("DropZone").style.display = "none";
	} catch(e) {
	}
}

function uploadCompleted(s) {
	document.forms["officeLinkForm"].elements["fileUpload"].value = "true";
	goToTopic(getCurrentNodeId());
	return true;
}

function setHighLight() {
	if (highlightedDocumentId != null) {
		$("#publication" + highlightedDocumentId).css("background-color", "#CCCCCC");
		window.location.href = "#publication" + highlightedDocumentId;
	}
}

function resetHighLight() {
	if (highlightedDocumentId != null) {
		$("#publication" + highlightedDocumentId).css("background-color", "#E3E3E3");
		highlightedDocumentId = null;
	}
}

var uploadWin = null;

function openDocumentUpload() {
	var url = context + "/Rofficelink/" + componentId + "/Main"
		+ "?actionId=" + ACTION_DOCUMENT_UPLOAD
		+ "&topicId=" + getCurrentNodeId();
	uploadWin = SP_openWindow(url, "DocumentUpload", "750", "300", "scrollbars=yes, resizable, alwaysRaised");
}

function waitForUpload() {
	window.setTimeout("checkUpload()", 500);
}

function checkUpload() {
	if (uploadWin != null && uploadWin.document.forms.length > 0) {
		window.setTimeout("checkUpload()", 500);
	} else {
		if (uploadWin != null && !uploadWin.closed) {
			uploadWin.close();
			uploadWin = null;
		}
		uploadCompleted(null);
	}
}

function getJsonUrl(action, id) {
	return context + "/json"
		+ "?componentId=" + componentId
        + "&action=" + action
        + "&userId=" + userId
        + "&userProfile=" + userProfile
        + "&spaceId=" + spaceId
        + "&id=" + id
        + "&language=" + language
        + "&ieFix=" + new Date().getTime();
}