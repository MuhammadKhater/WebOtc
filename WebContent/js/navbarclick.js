
//log out home
$("#Logoutclick").on('click', function() {
	// Remove all Page
	alert("LOGOUT");
    var TableSearch = document.getElementById("TableSearchId");
    while (TableSearch.firstChild) {
    	TableSearch.removeChild(TableSearch.firstChild);
    }
    var addNickName = document.getElementById("addNickName");
    while (addNickName.firstChild) {
    	addNickName.removeChild(addNickName.firstChild);
    }
    var addUserPhoto = document.getElementById("addUserPhoto");
    while (addUserPhoto.firstChild) {
    	addUserPhoto.removeChild(addUserPhoto.firstChild);
    }
    var publicChannelList = document.getElementById("publicChannelList");
    while (publicChannelList.firstChild) {
    	publicChannelList.removeChild(publicChannelList.firstChild);
    } 
    var privateChannelList = document.getElementById("privateChannelList");
    while (privateChannelList.firstChild) {
    	privateChannelList.removeChild(privateChannelList.firstChild);
    } 
    var channelNameInfo = document.getElementById("channelNameInfo");
    while (channelNameInfo.firstChild) {
    	channelNameInfo.removeChild(channelNameInfo.firstChild);
    }
    var msgWindow = document.getElementById("msgWindow");
    while (msgWindow.firstChild) {
    	msgWindow.removeChild(msgWindow.firstChild);
    }
    var msgWindow = document.getElementById("ReplyWindow");
    while (msgWindow.firstChild) {
    	msgWindow.removeChild(msgWindow.firstChild);
    } 
    websocket.close();
    document.getElementById('thePage').style.display='none';
    document.getElementById('homePage').style.display='none';
	document.getElementById('createPage').style.display='none';
	document.getElementById('searchPage').style.display='none';
	document.getElementById('searchTable').style.display='none';
	document.getElementById('ReplyWindow').style.display = "none";
	document.getElementById('MainDiv').style.display='';
	
	
});



// back home
$("#homeclick").on('click', function() {
	// Remove Table after subscribed
    var myNode = document.getElementById("TableSearchId");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
	document.getElementById('createPage').style.display='none';
	document.getElementById('searchPage').style.display='none';
	document.getElementById('searchTable').style.display='none';
	document.getElementById('homePage').style.display='';
	document.getElementById('createChannelLi').setAttribute('class', '');
	document.getElementById('homeLi').setAttribute('class', 'active');
	document.getElementById('searchChannelLi').setAttribute('class', '');
	});


//create channel 
$("#createChannelclick").on('click', function() {
	// Remove Table after subscribed
    var myNode = document.getElementById("TableSearchId");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
	document.getElementById('homePage').style.display='none';
	document.getElementById('searchPage').style.display='none';
	document.getElementById('searchTable').style.display='none';
	document.getElementById('createPage').style.display='';
	document.getElementById('createChannelLi').setAttribute('class', 'active');
	document.getElementById('homeLi').setAttribute('class', '');
	document.getElementById('searchChannelLi').setAttribute('class', '');
	});

// search channel 
$("#searchChannelclick").on('click', function() {
	// Remove Table after subscribed
    var myNode = document.getElementById("TableSearchId");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
	
	document.getElementById('homePage').style.display='none';
	document.getElementById('createPage').style.display='none';
	document.getElementById('searchTable').style.display='none';
	document.getElementById('searchPage').style.display='';
	document.getElementById('searchChannelLi').setAttribute('class', 'active');
	document.getElementById('createChannelLi').setAttribute('class', '');
	document.getElementById('homeLi').setAttribute('class', '');
	});