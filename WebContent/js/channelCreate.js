function closePrivateChannelonline(NickName){
	    	alert("unsubscribed Private Channel "+ NickName);
	    	var ButtonChannel = NickName;
	    	ButtonChannel.parentElement.removeChild(ButtonChannel);
	    	var TextChannel = document.getElementById(NickName+"Div");
	    	TextChannel.parentElement.removeChild(TextChannel);
	    	if(document.getElementById('channelNameInfo')== null){
	    		document.getElementById('channelNameInfo').innerHTML="";
	    	}
	    	document.getElementById('channelNameInfo').innerHTML="";
	    	document.getElementById('ReplyWindow').style.display = "none";	
}


function openPrivateChannelOnline(NickName) {
      	    	var nameChannel = document.createTextNode(NickName);
      	    	var BtnIdChannelName=NickName;
				var newLi = document.createElement("LI");
				var MainDiv = document.createElement("DIV");
				var newBtn = document.createElement("BUTTON");
				
				var unReadM = document.createElement("SPAN");
				unReadM.setAttribute('class', 'label label-danger');
				unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
				var NumUnRd = document.createTextNode("0");
				unReadM.appendChild(NumUnRd);
				
				var unReadC = document.createElement("SPAN");
				unReadC.setAttribute('class', 'label label-success');
				unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
				var NumUnRdC = document.createTextNode("0");
				unReadC.appendChild(NumUnRdC);
				
				
				MainDiv.setAttribute('class', 'hidden');
				MainDiv.setAttribute('id', BtnIdChannelName+"Div");
    		    
    		    newBtn.setAttribute('id', BtnIdChannelName);
    		    newBtn.setAttribute('class', 'btn btn-default btn-block');
    		    newBtn.style.textAlign = "left";
    		    newBtn.appendChild(nameChannel);
    		    newBtn.appendChild(unReadM);
    		    newBtn.appendChild(unReadC);
    		    
    		    document.getElementById("msgWindow").appendChild(MainDiv);
    		    newLi.appendChild(newBtn);
    		    newBtn.onclick = function() { clickTheChannel(this.id) };
    		    document.getElementById("privateChannelList").appendChild(newLi);
   
}


function openPrivateChannel(NickName) {
	
    var r = confirm("Press a button!\n Ok to open a private channel with "+NickName);
    if (r == true) {
       //// open the channel
       	var privateChannel= new Object();
       	privateChannel.NickName= document.getElementById("addNickName").innerHTML;
       	privateChannel.ID=NickName;
    
    	var sendMsg= JSON.stringify(privateChannel);
      	$.post("/OTC/CreatePrivateChannelServlet",sendMsg, function(response) {
      		var mydata = response;
      	    var resultFromServer=mydata.result;

      	    if(resultFromServer == false){
      	    	alert("Error");	
      	    }else if(resultFromServer == true){
      	    	alert("private channel created with " + NickName);
      	    	
      	    	var nameChannel = document.createTextNode(mydata.channel.NickName);
      	    	var BtnIdChannelName=mydata.channel.NickName;
				var newLi = document.createElement("LI");
				var MainDiv = document.createElement("DIV");
				var newBtn = document.createElement("BUTTON");
				
				var unReadM = document.createElement("SPAN");
				unReadM.setAttribute('class', 'label label-danger');
				unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
				var NumUnRd = document.createTextNode("0");
				unReadM.appendChild(NumUnRd);
				
				var unReadC = document.createElement("SPAN");
				unReadC.setAttribute('class', 'label label-success');
				unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
				var NumUnRdC = document.createTextNode("0");
				unReadC.appendChild(NumUnRdC);
				
				
				MainDiv.setAttribute('class', 'hidden');
				MainDiv.setAttribute('id', BtnIdChannelName+"Div");
    		    
    		    newBtn.setAttribute('id', BtnIdChannelName);
    		    newBtn.setAttribute('class', 'btn btn-default btn-block');
    		    newBtn.style.textAlign = "left";
    		    newBtn.appendChild(nameChannel);
    		    newBtn.appendChild(unReadM);
    		    newBtn.appendChild(unReadC);
    		    
    		    document.getElementById("msgWindow").appendChild(MainDiv);
    		    newLi.appendChild(newBtn);
    		    newBtn.onclick = function() { clickTheChannel(this.id) };
    		    document.getElementById("privateChannelList").appendChild(newLi);
      	    }	
      		
      	});
    } else {
       // do nathing
    }
}

function createPrivateChannelsList( myData){
	var nameChannel = document.createTextNode(myData.NickName);
	var BtnIdChannelName=myData.NickName;
	var newLi = document.createElement("LI");
	var MainDiv = document.createElement("DIV");
	
	var newBtn = document.createElement("BUTTON");
	
	var unReadM = document.createElement("SPAN");
	unReadM.setAttribute('class', 'label label-danger');
	unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
	var NumUnRd = document.createTextNode(myData.Unread);
	unReadM.appendChild(NumUnRd);
	
	var unReadC = document.createElement("SPAN");
	unReadC.setAttribute('class', 'label label-success');
	unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
	var NumUnRdC = document.createTextNode(myData.Replies);
	unReadC.appendChild(NumUnRdC);
	
	MainDiv.setAttribute('class', 'hidden');
	MainDiv.setAttribute('id', BtnIdChannelName+"Div");
   
    newBtn.setAttribute('id', BtnIdChannelName);
    newBtn.setAttribute('class', 'btn btn-default btn-block');
    newBtn.style.textAlign = "left";
    newBtn.appendChild(nameChannel);
    newBtn.appendChild(unReadM);
    newBtn.appendChild(unReadC);
    
    newBtn.onclick = function() { clickTheChannel(this.id) };
    
    // create message 
    document.getElementById("msgWindow").appendChild(MainDiv);
    for(l1 in myData.Messages){
    	createMsg(myData.Messages[l1], MainDiv);	
    }
    
    newLi.appendChild(newBtn);
    document.getElementById("privateChannelList").appendChild(newLi);
}



function createPublicChannelsList( myData){
	var nameChannel = document.createTextNode(myData.Name);
	var BtnIdChannelName=myData.Name;
	var newLi = document.createElement("LI");
	var MainDiv = document.createElement("DIV");
	
	var newBtn = document.createElement("BUTTON");
	
	var unReadM = document.createElement("SPAN");
	unReadM.setAttribute('class', 'label label-danger');
	unReadM.setAttribute('id', BtnIdChannelName+"MsgNT");
	var NumUnRd = document.createTextNode(myData.Unread);
	unReadM.appendChild(NumUnRd);
	
	var unReadC = document.createElement("SPAN");
	unReadC.setAttribute('class', 'label label-success');
	var NumUnRdC = document.createTextNode(myData.Replies);
	unReadC.setAttribute('id', BtnIdChannelName+"ReplyNT");
	unReadC.appendChild(NumUnRdC);
	
	MainDiv.setAttribute('class', 'hidden');
	MainDiv.setAttribute('id', BtnIdChannelName+"Div");
   
    newBtn.setAttribute('id', BtnIdChannelName);
    newBtn.setAttribute('class', ' btn btn-default btn-block');
    newBtn.style.textAlign = "left";
    newBtn.appendChild(nameChannel);
    newBtn.appendChild(unReadM);
    newBtn.appendChild(unReadC);
    
    newBtn.onclick = function() { clickTheChannel(this.id) };
    
    // create message
    document.getElementById("msgWindow").appendChild(MainDiv);
    
    for(l1 in myData.Messages){
    	createMsg(myData.Messages[l1], MainDiv);	
    }
   
    newLi.appendChild(newBtn);
    document.getElementById("publicChannelList").appendChild(newLi);
}




function clickTheChannel(nameChannel) {
	
    currentChannel=document.getElementById('channelNameInfo').innerHTML;
    if(currentChannel != ""){
    	document.getElementById(currentChannel+"Div").setAttribute('class', 'hidden');
    }
    resetNotification(nameChannel);
    document.getElementById(nameChannel+"Div").setAttribute('class', 'visible');
    var newChannelName = document.createTextNode(nameChannel);
    var element = document.getElementById("msgWindow");
    element.scrollTop = element.scrollHeight;
    document.getElementById('channelNameInfo').innerHTML=nameChannel;
    document.getElementById('ReplyWindow').style.display = "none";
    
   
}

function resetNotification(nameChannel){
	 var unRead = new Object();
	    unRead.NickName=document.getElementById("addNickName").innerHTML;
	    unRead.Chat=nameChannel;
	    
	    
	   
		var channelName=document.getElementById(nameChannel);
		var channelKind = channelName.parentNode;
		if(channelKind.parentNode.id == "publicChannelList"){
			$.post("/OTC/PublicResetSerlvet", JSON.stringify(unRead), function(response) {
		    	var mydata = response;
		  	    var resultFromServer=mydata.result;
		  	    if(resultFromServer == "false"){
		  	    	alert("Error");	
		  	    }else if(resultFromServer == "true"){
		  	    	document.getElementById(nameChannel+"ReplyNT").innerHTML="0";
		  	    	document.getElementById(nameChannel+"MsgNT").innerHTML="0";
		  	    }
		    });
		}else if(channelKind.parentNode.id == "privateChannelList"){
			$.post("/OTC/PrivateResetSerlvet", JSON.stringify(unRead), function(response) {
		    	var mydata = response;
		  	    var resultFromServer=mydata.result;
		  	    if(resultFromServer == "false"){
		  	    	alert("Error");	
		  	    }else if(resultFromServer == "true"){
		  	    	document.getElementById(nameChannel+"ReplyNT").innerHTML="0";
		  	    	document.getElementById(nameChannel+"MsgNT").innerHTML="0";
		  	    }
		    });
			
		}
}

