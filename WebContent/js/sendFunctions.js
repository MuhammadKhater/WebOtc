
function ReplyMsgBtn() {
	if (websocket != null){
		var newMessage= new Object();
		var IdMsg1=document.getElementById("TitleReplyMsg").innerHTML;
		var IdMsg=IdMsg1+"Div";
		
		newMessage.Sender= document.getElementById("addNickName").innerHTML;
		var MsgText=document.getElementById('ReplyMsgId').value;
		var MsgUserTag = document.getElementById(IdMsg).childNodes[1].childNodes[0].childNodes[0].id;
		newMessage.Message="@"+MsgUserTag + " "+MsgText;
		newMessage.Parent=IdMsg1;
		var channelName=document.getElementById("channelNameInfo").innerHTML;
		newMessage.Channel=channelName;
		var channelKind = document.getElementById(channelName).parentNode;
		if(channelKind.parentNode.id == "publicChannelList"){
			newMessage.ID="Public";
		}else if(channelKind.parentNode.id == "privateChannelList"){
			newMessage.ID="Private";
		}
		newMessage.Photo = "12e.jpg";
		var sendMsg= JSON.stringify(newMessage);
		 websocket.send(sendMsg);
	}
}

function sendMsgBtn() {
	if (websocket != null){
		var newMessage= new Object();
		newMessage.Sender= document.getElementById("addNickName").innerHTML;
		newMessage.Message=document.getElementById('MsgId').value;
		var channelName=document.getElementById("channelNameInfo").innerHTML;
		newMessage.Channel=channelName;
		newMessage.Parent="0";
		var imgSrc1 = document.getElementById("addUserPhoto").getElementsByTagName("IMG");
		var photoSrc = imgSrc1.src;
		newMessage.Photo = "12e.jpg";
		var channelKind = document.getElementById(channelName).parentNode;
		if(channelKind.parentNode.id == "publicChannelList"){
			newMessage.ID="Public";
		}else if(channelKind.parentNode.id == "privateChannelList"){
			newMessage.ID="Private";
		}
		var sendMsg= JSON.stringify(newMessage);
   		 websocket.send(sendMsg);
    }
}
