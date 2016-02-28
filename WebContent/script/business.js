var dailypayment = {
	_save:function(args){
		//Collect Data
		//send to server
		var grid = objects['mygrid'+args['id']];
		var divObj = document.getElementById('mygrid'+args['id']);
		var ids = divObj.getAttribute('ids');
		ids = ids.split(',');
		var pk = divObj.getAttribute('pk');
		var str = '';
		for(var i = 0; i<grid.rowsBuffer.length; i++){
			str += '[';
			var rowId = grid.rowsBuffer[i].idd;
			str += '[rowid:'+rowId+']';
			for(var k = 0; k<ids.length; k++){
				var curr = grid.cells(rowId,k).getValue();
				if(curr == null || curr == '' || curr==undefined)
					curr = '';
				str += '['+ids[k]+':'+curr+']';
			}
			str += ']';
		}
		var content = 'e='+dailypayment.getExecuter()+'&pk='+pk+'&op=s&data='+str;
		//Now send AjaxRequest;
		var url = '/BusinessServlet';
		var callBack = 'dailypayment._continueSave';
		Ajax.send (url, 'post', content, callBack, args);
	},
	_continueSave:function(args){
		alert(args['response']);
	},
	_delete:function(args){
		var id = args['id'];
		var divObject = document.getElementById('mygrid'+id); 
		var grid = objects['mygrid'+id];
		var selected = grid.getSelectedId();
		selected = selected.split(',');
		var pk = divObject.getAttribute('pk');
		var str = "";
		for(var i=0; i<selected.length; i++){
			str += '[';
			var rowId = grid.rowsBuffer[i].idd;
			str += '[rowid:'+rowId+']';
			str += '[id:'+selected[i]+']';
			str += ']';
		}
		var content = 'e='+dailypayment.getExecuter()+'&pk='+pk+'&op=d&data='+str;
		//Now send AjaxRequest;
		var url = '/BusinessServlet';
		var callBack = 'dailypayment._continueDelete';
		Ajax.send (url, 'post', content, callBack, args);
	},
	_new:function(args){
		var dateId = 10;
		var grid = objects['mygrid'+args['id']];
		if(objects["rn"] == null || objects["rn"] == '')
			objects["rn"] = -1;
		
		var rowId = objects["rn"];
		objects["rn"] = rowId - 1;
		grid.addRow(rowId, []);
		
		var _date = new Date();
		var _dateAsStr = _date.getFullYear()+'-'+(_date.getMonth()+1)+'-'+ _date.getDate();
		grid.cells(rowId, dateId).setValue(_dateAsStr);
		grid.selectRowById(rowId, true,true,true);
	},
	getExecuter:function(){
		return 'core.executers.business.DailyPayment';
	}
};

var customer = {
	selectcustomer:function(){
		var grid = objects['mygridcustomers_card'];
		var divObject = document.getElementById('mygridcustomers_card'); 
		var selected = grid.getSelectedId();
		if(selected != null && selected != '' && selected != undefined){
			var content = 'e='+customer.getExecuter()+'&op=setcustomer&data='+selected;
			var url = '/BusinessServlet';
			var callBack = 'customer._continueSelectCustomer';
			var args = [];
			Ajax.send (url, 'post', content, callBack, args);
		}
		else{
			alert('PleaseSelectCustomer');
		}
	},
	_continueSelectCustomer:function(args){
		try{
			var response = args['response'];
			if(response == 'ok'){
				alert('Done...');
			}
			else{
				alert('Not Done...');
			}
		}
		catch(e){
			alert(e.message);
		}
	},
	getExecuter:function(){
		return 'core.executers.business.Customer';
	},
	newinvoiceselected:function(args){
		//alert('newinvoiceselected');
	},
	payingselected:function(args){
		//alert('payingselected');
	},
	archiveinvoiceselected:function(args){
		//alert('archiveinvoiceselected');
	},
	retaininvselected:function(args){
		//alert('retaininvselected');
	},
	statementselected:function(args){
		//alert('statementselected');
	}
};