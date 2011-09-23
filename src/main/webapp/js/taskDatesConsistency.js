var MILLISENCONDS_IN_A_DAY = 86400000;

function checkDatesConsistency(){
	
	var dateHasBeenModified = document.formTask.datesModified.value;
	if(dateHasBeenModified=="true"){
		hideDateError();
		
		var startDateOption = document.getElementById("start").value;
	    var endDateOption = document.getElementById("end").value;
		
	    if("later"!=startDateOption && "later"!=endDateOption){
	    	if("today"==startDateOption){
	    		return checkEndAfterStartDate(getToday0001Date());
	    	} else if("tomorrow"==startDateOption){
	    		if("today"==endDateOption){
	    			//error
	    			dateError("Check Your Dates. Start date should be previous to end date");
	    			return false;
	    		} else if("specific"==endDateOption){
	    			//check end date >= tomorrow 00:01
	    			return checkEndAfterStartDate(getTomorrow0001Date());
	    		}
			} else if("thisweek"==startDateOption){
				if("today"==endDateOption){
	    			//error
					dateError("Check Your Dates. Start date should be previous to end date");
					return false;
				} else if("tomorrow"==endDateOption){
	    			//error
					dateError("Check Your Dates. Start date should be previous to end date");
					return false;
	    		} else if("specific"==endDateOption){
	    			//check end date >= end of week 00:01
	    			return checkEndAfterStartDate(getEndOfWeek0001Date());
	    		}
			} else if("nextweek"==startDateOption){
				if("today"==endDateOption){
	    			//error
					dateError("Check Your Dates. Start date should be previous to end date");
					return false;
				} else if("tomorrow"==endDateOption){
	    			//error
					dateError("Check Your Dates. Start date should be previous to end date");
					return false;
				} else if("thisweek"==endDateOption){
	    			//error
					dateError("Check Your Dates. Start date should be previous to end date");
					return false;
	    		} else if("specific"==endDateOption){
	    			//check end date >= end of next week 00:01
	    			return checkEndAfterStartDate(getEndOfNextWeek0001Date());
	    		}
			} else if("specific"==startDateOption){
				
				if(!checkStartDateIsFuture()){
					dateError("Check Your Dates. Start date can't be in the past.");
					return false;
				}
				
				if("today"==endDateOption){
					//check start date <= today 23:59
					return checkStartBeforeDate(getToday2359Date());
				} else if("tomorrow"==endDateOption){
					//check start date <= tomorrow 23:59
					return checkStartBeforeDate(getTomorrow2359Date());
				} else if("thisweek"==endDateOption){
					//check start date <= end of week 23:59
					return checkStartBeforeDate(getEndOfWeek2359Date());
				} else if("nextweek"==endDateOption){
					//check start date <= end of next week 23:59
					return checkStartBeforeDate(getEndOfNextWeek2359Date());
	    		} else if("specific"==endDateOption){
	    			if(!checkEndDateIsFuture()){
						dateError("Check Your Dates. End date can't be in the past.");
						return false;
					}
	    			
	    			//check end date >= start date 
	    			return checkStartDateBeforeEndDate();
	    		}
	    	}
	    }
	}
	return true;
}

function checkStartDateIsFuture(){
	var start = getStartDate();
	var now = new Date();
	if(start < now){
		return false;
	} else{
		return true;
	}
}

function checkEndDateIsFuture(){
	var end = getEndDate();
	var now = new Date();
	if(end < now){
		return false;
	} else{
		return true;
	}
}

function checkEndAfterStartDate(startDate){
    var end = getEndDate();
	if(end < startDate){
		// error
		dateError("Check Your Dates. Start date should be previous to end date");
		return false;
	} else{
		return true;
	}
}

function checkStartBeforeDate(endDate){
    var start = getStartDate();
    if(endDate < start){
		// error
    	dateError("Check Your Dates. Start date should be previous to end date");
		return false;
	} else{
		return true;
	}
}

function checkStartDateBeforeEndDate(){
	var startDate = getStartDate();
	var endDate = getEndDate();
	if(startDate > endDate){
		// error
		dateError("Check Your Dates. Start date should be previous to end date");
		return false;
	} else{
		return true;
	}
}

function getStartDate(){
	var startDate = document.getElementById("startDate").value;
    var startDay  = parseInt(startDate.substring(0,2),10);
    var startMonth = parseInt(startDate.substring(3,5),10);
    var startYear  = parseInt(startDate.substring(6,10),10);
    var start = new Date(startYear, startMonth-1, startDay);
    
    var startTime = document.getElementById("startTime").value;
    if(startTime.length==5){
    	var startHour = parseInt(startTime.substring(0,2),10);
    	var startMinutes = parseInt(startTime.substring(3,5),10);
    	start.setHours(startHour);
    	start.setMinutes(startMinutes);
    } else if(startTime.length>0){
    	document.getElementById("startTime").value = "";
    } else{
    	start.setHours("00");
    	start.setMinutes("01");
    	start.setSeconds("00");
    }
	return start;
}

function getEndDate(){
	var endDate = document.getElementById("endDate").value;
    var endDay  = parseInt(endDate.substring(0,2),10);
    var endMonth = parseInt(endDate.substring(3,5),10);
    var endYear  = parseInt(endDate.substring(6,10),10);
    var end = new Date(endYear, endMonth-1, endDay); 
    
    var endTime = document.getElementById("endTime").value;
	if(endTime.length==5){
		var endHour = parseInt(endTime.substring(0,2),10);
    	var endMinutes = parseInt(endTime.substring(3,5),10);
    	end.setHours(endHour);
    	end.setMinutes(endMinutes);
	} else if(endTime.length>0){
    	document.getElementById("endTime").value = "";
    } else{
    	end.setHours("23");
    	end.setMinutes("59");
    	end.setSeconds("00");
    }
	return end;
}

function getToday2359Date(){
	var today = new Date();
	today.setHours("23");
	today.setMinutes("59");
	today.setSeconds("00");
	return today;
}

function getToday0001Date(){
	var today = new Date();
	today.setHours("00");
	today.setMinutes("01");
	today.setSeconds("00");
	return today;
}

function getTomorrow2359Date(){
	var tomorrow = new Date();
	tomorrow.setMilliseconds(today.getMilliseconds()+MILLISENCONDS_IN_A_DAY);
	tomorrow.setHours("23");
	tomorrow.setMinutes("59");
	tomorrow.setSeconds("00");
	return tomorrow;
}

function getTomorrow0001Date(){
	var tomorrow = new Date();
	tomorrow.setMilliseconds(today.getMilliseconds()+MILLISENCONDS_IN_A_DAY);
	tomorrow.setHours("00");
	tomorrow.setMinutes("01");
	tomorrow.setSeconds("00");
	return tomorrow;
}

function getEndOfWeek2359Date(){
	var daysBeforeEndOfWeekValue = document.getElementById("daysBeforeEndOfWeek").value;
	var daysBeforeEndOfWeek  = parseInt(daysBeforeEndOfWeekValue,10);
	
	var endOfWeek = new Date();
	endOfWeek.setMilliseconds(today.getMilliseconds()+daysBeforeEndOfWeek*MILLISENCONDS_IN_A_DAY);
	endOfWeek.setHours("23");
	endOfWeek.setMinutes("59");
	endOfWeek.setSeconds("00");
	return endOfWeek;
}

function getEndOfWeek0001Date(){
	var daysBeforeEndOfWeekValue = document.getElementById("daysBeforeEndOfWeek").value;
	var daysBeforeEndOfWeek  = parseInt(daysBeforeEndOfWeekValue,10);
	
	var endOfWeek = new Date();
	endOfWeek.setMilliseconds(today.getMilliseconds()+daysBeforeEndOfWeek*MILLISENCONDS_IN_A_DAY);
	endOfWeek.setHours("00");
	endOfWeek.setMinutes("01");
	endOfWeek.setSeconds("00");
	return endOfWeek;
}

function getEndOfNextWeek2359Date(){
	var daysBeforeEndOfNextWeekValue = document.getElementById("daysBeforeEndOfNextWeek").value;
	var daysBeforeEndOfNextWeek  = parseInt(daysBeforeEndOfNextWeekValue,10);
	
	var endOfNextWeek = new Date();
	endOfNextWeek.setMilliseconds(today.getMilliseconds()+daysBeforeEndOfNextWeek*MILLISENCONDS_IN_A_DAY);
	endOfNextWeek.setHours("23");
	endOfNextWeek.setMinutes("59");
	endOfNextWeek.setSeconds("00");
	return endOfNextWeek;
}

function getEndOfNextWeek0001Date(){
	var daysBeforeEndOfNextWeekValue = document.getElementById("daysBeforeEndOfNextWeek").value;
	var daysBeforeEndOfNextWeek  = parseInt(daysBeforeEndOfNextWeekValue,10);
	
	var endOfNextWeek = new Date();
	endOfNextWeek.setMilliseconds(today.getMilliseconds()+daysBeforeEndOfNextWeek*MILLISENCONDS_IN_A_DAY);
	endOfNextWeek.setHours("00");
	endOfNextWeek.setMinutes("01");
	endOfNextWeek.setSeconds("00");
	return endOfNextWeek;
}

function dateError(text){
	 showDateError(text);
	 
	//alert("Check Your Dates. End date should be previous to start date");
}

function showDateError(text){
	var element = document.getElementById("datesError");
	element.innerHTML = text;
	element.style.visibility='visible';
	element.style.display='';
	
	scrollToElement(element);
}

function scrollToElement(element){
	window.scrollTo(0,findYPos(element));
}

function findYPos(obj) {
	var curtop = 0;
	if (obj.offsetParent) {
		do {
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	return [curtop];
	}
}

function hideDateError(){
	var element = document.getElementById("datesError");
	element.style.visibility='hidden';
	element.style.display='none';
}