var debug = true;
function alertMsg(msg, type){
	if(debug == true){
		if(type == null || type == '' || type == undefined)
			type = 'info';
		alert(type+':'+msg);
	}
}
var objects = new Array();
function executeFunctionByName(functionName, context , argsArray) {
	try{
		var args = Array.prototype.slice.call(arguments).splice(2);
		var namespaces = functionName.split(".");
		var func = namespaces.pop();
		for(var i = 0; i < namespaces.length; i++) {
			context = context[namespaces[i]];
		}
		return context[func].apply(this, args);
	}
	catch(e){
		alertMsg(e.message, 'executeFunctionByName');
	}
}







//===================================================================
var Generator = {
		init:function(){
			try{
				var template = document.getElementById('templates');
				if(template != null && template != '' && template != undefined){
					template = template.value;
					var items =template.split(','); 
					for(var i=0; i<items.length; i++){
						var item = document.getElementById(''+items[i]);
						var template = item.getAttribute("template");
						var url = '/ComponentGenerator?template='+template;
						var callBack = 'Generator.continureInit';
						var args = new Array();
						args["id"] = items[i];
						Ajax.send (url,'get',null,callBack, args);
					}
				}
			}
			catch(e){
				alertMsg(e.message, 'generator, init');
			}
		},
		continureInit:function(args/*response, itemId*/){	
			var response = args["response"];
			var rr = response.split("@@");
			response = rr[0];
			var itemId = args["id"];
			if(response != null){
				var content = document.getElementById(itemId);
				content.innerHTML = response;
			}
			try{
				var functionsToDo = rr[1];
				if(functionsToDo != null && functionsToDo != '' && functionsToDo != undefined){
					functionsToDo = functionsToDo.split(";");
					for(var i=0; i< functionsToDo.length; i++){
						var currf = functionsToDo[i];
						if(currf != '' && currf != undefined && currf != null){
							currf = currf.split(":");
							var a = new Array();
							a["id"] = currf[1];
							executeFunctionByName(currf[0], window, a);
						}
					}
				}
			}
			catch(e){
				alertMsg(e.message, 'generator, continue init');
			}
		}
};




//===================================================================
var Ajax = {
	get:function(){
		try{
			return new XMLHttpRequest();
		}
		catch(e){
			try{
				return new ActiveXObject("Msxml2.XMLHTTP");
			}
			catch(ee){
				try{
					return new ActiveXObject("Msxml3.XMLHTTP");
				}
				catch(eee){
					return new ActiveXObject("Microsoft.XMLHTTP");
				}
			}
		}
		return null;
	},
	send:function(url,method,content,callback, argsArray){
		try{
			if(argsArray == null || argsArray == undefined)
				argsArray = new Array();
			var req = Ajax.get();
			if(req != null){
				if(method == null || method=='' || method == undefined)
					method = 'post';
				
				var rootURL = window.location.href.toString().split(window.location.host)[1];
				rootURL = rootURL.substring(1);
				rootURL = rootURL.substring(0, rootURL.indexOf('/'));
				url = '/'+rootURL+'/'+url;
				req.open(method, url, true);
				req.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
				req.onreadystatechange = function() {
					if (req.readyState == 4) {
						argsArray["response"] = req.responseText;
						executeFunctionByName(callback, window, argsArray);
					}
				};
				
				if(method == 'get')
					req.send();
				else
					req.send(content);
			}
		}
		catch(e){
			alertMsg(e.message, 'ajax,send');
		}
	}
};





//===================================================================
var defaulthandler = {
	login:function(event){
		//send ajax request of used name and password
		//callback is 'defaulthandler.continuelogin'
		var userName = document.getElementById("username");
		var password = document.getElementById("password");
		var content = "content=username:"+userName.value+"@@password:"+password.value;
		var url = "/LoginExecuter?cmd=login";
		var callback = "defaulthandler.continuelogin";
		Ajax.send(url, "post", content, callback);
	},
	logoff:function(){
		window.location.href = "logoff.jsp";
	},
	continuelogin:function(args){
		//if repsonse is ok, then go to home page
		//else display error message
		var response = args["response"];
		if(response == 'ok'){
			//alert("ok");
			var rootURL = window.location.href.toString().split(window.location.host)[1];
			rottURL = rootURL.substring(0, rootURL.lastIndexOf("/"));
			window.location.href = rottURL+"/home.jsp";
		}
		else{
			alert(response);
		}
	},
	displaylov:function(id){
		try{
			//get Lov resouce
			//send request to server to get data
			var name = 'lov'+id+'popup';
			var popup = document.getElementById(name);
			var cls =  popup.getAttribute("class");
			if(cls == 'popupArea')
				popup.setAttribute("class", "popupArea hidden");
			else{
				popup.setAttribute("class", "popupArea");
				//get Data
				var resource = 'lov'+id+'resource';
				resource = document.getElementById(resource);
				resource = resource.value;
				if(resource != null){
					var args = resource.split(";");
					var args1 = new Array();
					if(args != null){
						 
						 for(var i=0;i<args.length; i++){
							 var itm = args[i];
							 itm = itm.split(":");
							 args1[itm[0]] = itm[1];
						 }
					}
					var srcName = args1['srcname'];
					var dbId = args1['dbid'];
					var dbName = args1['dbname'];
					var where = args1['where'];
					if(	srcName != null && srcName != '' && srcName != undefined &&
						dbId != null && dbId != '' && dbId != undefined &&
						dbName != null && dbName != '' && dbName != undefined  ){
						var url = '/FillLov?srcname='+srcName+'&dbid='+dbId+'&dbname='+dbName+'&where:'+where;
						var callBack = 'defaulthandler.continuedisplaylov';
						args1['id'] = id;
						Ajax.send (url,'get',null,callBack, args1);
					}
				}
			}
		}
		catch(e){
			alertMsg(e.message, 'default, handler.display lov');
		}
	},
	continuedisplaylov:function(args){
		try{
			var id = args['id'];
			//var name = 'lov'+id+'popup';
			//var popup = document.getElementById(name);
			var tblOfContent = 'lov'+id+'content';
			tblOfContent = document.getElementById(tblOfContent);
			var response = args['response'];;
			response = response.split(";");
			var str = "";
			for(var i=0; i<response.length; i++ ){
				var itm = response[i];
				itm = itm.split(",");
				str += "<tr onclick=\"defaulthandler.setselitmfrmlov('"+itm[0]+"','"+itm[1]+"','"+args["pageid"]+"','"+args["pagename"]+"','"+args["id"]+"');\">";
				str += 	 "<td >";
				str += 		itm[0];
				str += 	 "</td>";
				str += 	 "<td>";
				str += 	 	itm[1];
				str += 	 "</td>";
				str += "</tr>";
			}
			tblOfContent.innerHTML = str;
		}
		catch(e){
			alertMsg(e.message, 'defaulthandler.continuedisplaylov');
		}
	},
	setselitmfrmlov:function (dbId,dbName,pageId,pageName, id){
		try{
			var pId = document.getElementById(pageId);
			var pName = document.getElementById(pageName);
			pId.value = dbId;
			pName.value = dbName;
			defaulthandler.displaylov(id);
		}
		catch(e){
			alertMsg(e.message, 'defaulthandler.setselitemfromlov');
		}
	}
}; 




//===================================================================
var tab = {
		tabClicked:function(tabId){
			try{
				var tabHeader = document.getElementById('tabheader'+tabId);
				var tabParent = tabHeader.parentNode;
				for(var i=0; i<tabParent.childNodes.length; i++)
					tabParent.childNodes[i].setAttribute("class","tabheader");
				tabHeader.setAttribute("class", "tabheader tabheader_sel");
				var tabBody = document.getElementById('tabbody'+tabId);
				//var tabBodyParent = tabBody.parentNode;
				var x = tabBody;
				while(x.previousSibling != null && x.previousSibling != '' && x.previousSibling != undefined){
					x = x.previousSibling;
				}
				while(x.nextSibling != null && x.nextSibling != '' && x.nextSibling != undefined){
					x.setAttribute("class", "hidden");
					x = x.nextSibling;
				}
				tabBody.setAttribute("class", "tabbody");
				var selected = tabHeader.getAttribute("selected");
				if(selected != null && selected != '' && selected != undefined){
					var args = [];
					executeFunctionByName(selected, window, args);
				}
			}
			catch(e){
				alertMsg(e.message, 'tab.tablclicked');
			}
		}
};



//===================================================================
var accordion = {
		accordionClicked:function(accordionId){
			var accordionBody = document.getElementById('accordionbody'+accordionId);
			var c = document.getElementById('c'+accordionId);
			var itemHeader = document.getElementById('itemHeader'+accordionId);
			var cls = accordionBody.getAttribute("class");
			if(cls.contains('hidden')){
				accordionBody.setAttribute("class", "accordion");
				itemHeader.setAttribute("class", "accordion_header_item selected");
				c.innerHTML = "-";
			}
			else{
				accordionBody.setAttribute("class", "accordion hidden");
				itemHeader.setAttribute("class", "accordion_header_item");
				c.innerHTML = "+";
			}
		}
};


//=====================================================================
var Table = {
		create: function(args){
			var divObject = document.getElementById('mygrid'+args["id"]);
			var skin = divObject.getAttribute("skin"); 
			var colsort= divObject.getAttribute("colsort");
			var colstype = divObject.getAttribute("colstype");
			var colsalign = divObject.getAttribute("colsalign");
			var initwidths = divObject.getAttribute("initwidths");
			var header = divObject.getAttribute("header");
			//var filldata = divObject.getAttribute("filldata");
			//var update = divObject.getAttribute("update");
			//var _delete = divObject.getAttribute("delete");
			//var insert = divObject.getAttribute("insert");
			//var dbtable = divObject.getAttribute("dbtable");
			//var title = divObject.getAttribute("title");
			var combos = divObject.getAttribute("combos");
			
			var grid = new dhtmlXGridObject('mygrid'+args["id"]);	
			grid.setImagePath('dhtml/codebase/imgs/');
			grid.setHeader(header);
			grid.setInitWidths(initwidths);
			grid.setColAlign(colsalign);
			grid.setColTypes(colstype);
			grid.setColSorting(colsort);
			grid.enableAutoHeigth();
			try{
				//:2:[7,cat 2][8,cat 3][9,cat4][10,Cat No 5][5,cat 1][11,Cat No ;3:[5,Stock No 5][2,Stock No 2][6,Stocl No6][7,Stock No
				combos = combos.split(";");
				for(var i=0; i<combos.length; i++){
					try{
						var currC = combos[i].split(":");
						var colId = currC[0];
						var __data = currC[1];
						__data = __data.split("][");
						var gcombo = grid.getCombo(colId);
						for(var kk=0; kk<__data.length;kk++){
							var curr__Data = __data[kk];
							if(curr__Data.startsWith('['))
								curr__Data = curr__Data.substring(1,curr__Data.length);
							if(curr__Data.endsWith('['))
								curr__Data = curr__Data.substring(0,curr__Data.length-1);
						
							curr__Data = curr__Data.split(",");
							gcombo.put(curr__Data[0], curr__Data[1]);
						}
						gcombo.save();
					}
					catch(eeee){;}
				}
			}
			catch(eee){
				//alertMsg(eee.message, 'err');
			}
			grid.init();
			grid.setSkin(skin);
			//atachevent
			try{
				var onchangefields = divObject.getAttribute('onchangefields');
				if(onchangefields != null && onchangefields != '' && onchangefields != undefined){
					//split actions
					onchangefields = onchangefields.split(',');
					var colsArr = new Array();
					for(var c = 0; c<onchangefields.length; c++){
						var currColEvent = onchangefields[c].split(':');
						colsArr[currColEvent[0]] = currColEvent[1];
					}
						
					grid.attachEvent("onEditCell", function(cInd,rId,oValue,nValue)
						{
							if(nValue != undefined && nValue != null){
								var args1 = new Array();
								args1["id"] = args["id"];
								args1["rowId"] = rId;
								args1["colId"] = cInd;
								args1["oldVal"] = oValue;
								args1["newVal"] = nValue;
								executeFunctionByName('Table.checkForChange', window, args1);
							}
							return true;
						}
					);
				}
				
			}
			catch(eee){
				alertMsg(eee.message, 'grid, attachevent');
			}
			objects['mygrid'+args["id"]] = grid;
			
			
			//if search creteria has date fields, attach them
			try{
				var dsf = document.getElementById(args["id"]+'datefields');
				dsf = dsf.value;
				dsf = dsf.split(',');
				for(var i=0; i<dsf.length; i++){
					var currDateInp = dsf[i];
					new dhtmlXCalendarObject(currDateInp);
				    //myCalendar.show();
				}
			}
			catch(exx){
				alertMsg(exx);
			}
		},
		recreate : function(id){
			try{
				var divObj = document.getElementById('mygrid'+id);
				var name = divObj.getAttribute('uniquename');
				//alert(name);
				//send ajax request to reconstruct the object again
				//ComponentGenerator
				//cmd=recreate&template=xx.xml&id=yy.mm.rr....
				//get tamplate
				var o = divObj;
				while(o.getAttribute('template') == null)
					o = o.parentNode;
				var templateName = o.getAttribute('template');
				
				var url = '/ComponentGenerator?cmd=recreate&template='+
								templateName+
								'&id='+name+'&type=table';
				var callBack = 'Table.continurecreate';
				var args = new Array();
				args["id"] = id;
				Ajax.send (url,'get',null,callBack, args);
				
			}
			catch(ex){
				alertMsg(ex.message, 'Table.recreate');
			}
			
			var args = new Array();
			args["id"] = id;
			Table.create(args);
		},
		continurecreate:function(args){
			try{
				var response = args['response'];
				response = response.split('@@');
				var id = response[0];
				var content = response[1];
				var obj = document.getElementById(id);
				obj.innerHTML = content;
				Table.create(args);
				alert('RefreshOperationIsFinished');
			}
			catch(ex){
				alertMsg(ex.message, 'Table.continuerecreate');
			}
		},
		_save:function(id){
			var str = "";
			try{
				//mygridstk_itm_table_store
				var divObject = document.getElementById('mygrid'+id);
				var grid = objects['mygrid'+id];
				var ids = divObject.getAttribute("ids");
				var colsTypes = divObject.getAttribute("colstype");
				
				var dbTable = divObject.getAttribute("dbtable");
				var pk = divObject.getAttribute("pk");
				if(pk != null && pk != '' && pk != undefined){
					ids = ids.split(',');
					colsTypes = colsTypes.split(',');
					for(var i=0; i<grid.getRowsNum(); i++){
						try{
							var temp = "";
							var rowId = grid.rowsCol[i].idd;
							for(var k=0; k<grid.getColumnsNum(); k++){
								var currColType = colsTypes[k];
								var key = ids[k];
								if( (key == pk) || (currColType != 'ro')){
									var val = grid.cells(rowId,k).getValue();
									temp += key+":::"+val+"@@";
								}
							}
							if(temp.length > 2)
								temp = temp.substring(0, temp.length-2);
								str += temp+";;";
						}
						catch(eee){
							;
						}
					}
					if(str.length > 2)
						str = str.substring(0, str.length-2);

					//-------------------------------------
					var args = new Array();
					args["id"] = id;
					var savemethod = divObject.getAttribute("savemethod");
					if(savemethod == null || savemethod == '' || savemethod == undefined)
						savemethod = 'default';
					
					if(savemethod == 'default'){
						var url = 	'/DataExecuter';
						var callBack = 'Table._coninuteSave';
						//url = encodeURIComponent(url);
						var content = 'mode='+savemethod+'&kind=save&table='+dbTable+
										'&ids=&where=&orderby=&obj=table&data='+str+'&pk='+pk;
						Ajax.send (url,'post',content,callBack, args);
					}
					else{
						args["mode"] = savemethod;
						args["kind"] = "save";
						args["ids"] = "";
						args["where"] = "";
						args["orderby"] = "";
						args["obj"]="table";
						args["data"]=str;
						args["pk"]=pk;
						executeFunctionByName(savemethod, window, args);
					}
				}
				else{
					alert('NoPKForTable');
				}
			}
			catch(e){
				alertMsg(e.message, 'Table._save');
			}
		},
		_coninuteSave:function(args){
			try{
				alert('SaveIsDoneAndNowDataWillBeRest');
				var id = args["id"];
				Table._search(id);
			}
			catch(ee){
				alertMsg(ee.message, 'Table._continueSave');
			}
		},
		_new:function(id){
			try{
				//alert('Table._new'+id);
				var divObj = document.getElementById('mygrid'+id);
				var newmethod = divObj.getAttribute('newmethod');
				if(newmethod == null || newmethod==undefined || newmethod == '')
					newmethod = 'default';
				
				if(newmethod == 'default'){
					var grid = objects['mygrid'+id];
					if(objects["rn"] == null || objects["rn"] == '')
						objects["rn"] = -1;
					
					var rowId = objects["rn"];
					objects["rn"] = rowId - 1;
					grid.addRow(rowId, []);
					grid.selectRowById(rowId, true,true,true);
				}
				else{
					var args = new Array();
					args["id"] = id;
					executeFunctionByName(newmethod, window, args);
				}
			}
			catch(e){
				alertMsg(e.message, 'Table._new');
			}
		},
		_delete:function(id){
			try{
				if(window.confirm("ConfirmDelete")){
					var divObject = document.getElementById('mygrid'+id);
					var dbTable = divObject.getAttribute("dbtable");
					var pk = divObject.getAttribute("pk");
					var grid = objects['mygrid'+id];
					var selected = grid.getSelectedId();
					//alert(selected);
					//grid.deleteSelectedItem();
					var deletemethod = divObject.getAttribute("deletemethod");
					if(deletemethod == null || deletemethod == '' || deletemethod == undefined)
						deletemethod = 'default';
					var args = new Array();
					args["id"] = id;
					if(deletemethod == 'default'){
						var url = 	'/DataExecuter?mode='+deletemethod+'&kind=delete&table='+dbTable+
						'&ids=&where=&orderby=&obj=table&data='+selected+'&pk='+pk;
						var callBack = 'Table._coninuteDelete';
						Ajax.send (url,'get',null,callBack, args);
					}
					else{
						args["mode"]=deletemethod;
						args["kind"]="delete";
						args["table"]=dbTable;
						args["ids"]="";
						args["where"]="";
						args["orderby"]="";
						args["obj"]="table";
						args["date"]=selected;
						executeFunctionByName(deletemethod, window, args);
					}
				}
			}
			catch(e){
				alertMsg(e.message, 'Table._delete');
			}
		},
		_coninuteDelete:function(args){
			try{
				alert('DeleteIsDoneAndDataWillBeReset');
				var id = args["id"];
				Table._search(id);
			}
			catch(e){
				
			}
		},
		_search:function(id){
			var divObject = document.getElementById('mygrid'+id);
			try{
				var searchmethod = divObject.getAttribute('searchmethod');
				if(searchmethod == null || searchmethod == '' || searchmethod == undefined)
					searchmethod = 'default';
				//collect search criteria
				//collect table data
				var ids = divObject.getAttribute("ids");
				var dbTable = divObject.getAttribute("dbtable");
				var where1 = divObject.getAttribute("where");
				if(where1 == null || where1 == undefined || where1 == 'null')
					where1 = '';
				var orderby = divObject.getAttribute("orderby");
				if(orderby == null || orderby == undefined)
					orderby = '';
				var where = Table._collectSearch(id);
				if(where == null || where == undefined || where == '')
					where = where1;
				else if(where1 != '')
					where += " and "+where1;
				
				var searchmethod = divObject.getAttribute("searchmethod");
				if(searchmethod == null || searchmethod == '' || searchmethod == undefined)
					searchmethod = 'default';
				
				var args = new Array();
				args["id"] = id;
				if(searchmethod == 'default'){
					var url = 	'/DataExecuter';
					var content = 'mode='+searchmethod+'&kind=search&table='+dbTable+
						'&ids='+ids+'&where='+where+'&orderby='+orderby+'&obj=table';
					var callBack = 'Table._continueSearch';
					Ajax.send (url,'post',content,callBack, args);
				}
				else{
					args["mode"]=searchmethod;
					args["kind"]="search";
					args["table"]=dbTable;
					args["ids"]=ids;
					args["where"]=where;
					args["orderby"]=orderby;
					args["obj"]="table";
					executeFunctionByName(searchmethod, window, args);
				}
			}
			catch(e){
				alertMsg(e.message, 'Table._search');
			}
		},
		_collectSearch:function(id){
			try{
				var where = "";
				var divObject = document.getElementById('mygrid'+id);
				var searchFields = divObject.getAttribute('searchFields');
				searchFields = searchFields.split(',');
				for(var i=0; i<searchFields.length; i++){
					try{
						var x = searchFields[i].split("-");
						var itm = x[0];
						var sc = x[1];
						var obj = document.getElementById(itm);
						obj = obj.value;
						var key = itm;
						key = key.substring("search_".length, key.length);
						key = key.substring(0, key.lastIndexOf("_"));
						if(obj != null && obj != '' && obj != undefined){
							 where += key+","+sc+","+obj+";";
						}
					}
					catch(ee){;}
				}
				if(where.length > 1)
					where = where.substring(0, where.length - 1);
				return where;
			}
			catch(e){
				alertMsg(e.message, 'Table._collectSearch');
			}
			return "";
		},
		_continueSearch:function(args){
			/* Get div namce
			 * get name of table
			 * request to fill data 
			 * after reponde fill data
			*/
			try{
				var divObject = document.getElementById('mygrid'+args["id"]);
				var data = args["response"];
				var grid = objects['mygrid'+args["id"]];
				grid.clearAll();
				var drows = data.split("@@");
				for(var i=0; i<drows.length; i++){
					var dcells = drows[i].split(";;");
					var dcellsArr = new Array();
					for(var k=0; k<dcells.length; k++){
						dcellsArr[k] = dcells[k];
					}
					grid.addRow(dcells[0],dcellsArr);
				}
			}
			catch(e){
				alertMsg(e.message, 'Table._continueSearch');
			}
		},
		checkForChange : function(args){
			//Collect fields
			//send to server
			try{
				var gId = args['id'];
				var rowId = args['rowId'];
				gId = 'mygrid'+gId;
				var grid = objects[''+gId];
				if(grid.cell.wasChanged == true){
					var m='';
					for(var  k=0; k<grid.getColumnsNum(); k++){
						var val = grid.cells(rowId, k).getValue();
						if(val == null || val == undefined)
							val = '';
						m+= '['+k+':'+val+']';
					}
					//if(m.length > 1)
					//	m = m.substring(0, m.length - 1);
					if(m.length > 1){
						var id = args['id'];
						var divObj = document.getElementById('mygrid'+id);
						var name = divObj.getAttribute('uniquename');
						var o = divObj;
						while( (o.getAttribute('template')) == null){
							o = o.parentNode;
						}
						var templateName = o.getAttribute('template');
						var colIds = divObj.getAttribute('ids');
						var url = 	'/ExpressionServlet';
						var content = 'cmd=tableapplyall&val='+m+'&template='+templateName+'&id='+name+'&colIds='+colIds;
						var callBack = 'Table.continueCheckForChange';
						Ajax.send (url,'post',content,callBack, args);
					}
				}
			}
			catch(e){
				alertMsg(e.message, 'Table.checkForChange');
			}
		},
		continueCheckForChange : function(args){
			//get result
			//apply changes in columns
			try{
				var response = args['response'];
				//alert(response);
				if(response.startsWith('['))
					response = response.substring(1);
				if(response.endsWith(']'))
					response = response.substring(0, response.length - 1);
				response = response.split('][');
				//----------------------------------------------------
				var gId = args['id'];
				gId = 'mygrid'+gId;
				var grid = objects[''+gId];
				var rowId = args['rowId'];
				for(var i=0; i<response.length; i++){
					var curr = response[i];
					curr = curr.split(':');
					var k = curr[0];
					var val = '';
					if(curr.length>1)
						val = curr[1];
					grid.cells(rowId, k).setValue(val);
				}
			}
			catch(e){
				alertMsg(e.message, 'continueCheckFromChange');
			}
		}
};