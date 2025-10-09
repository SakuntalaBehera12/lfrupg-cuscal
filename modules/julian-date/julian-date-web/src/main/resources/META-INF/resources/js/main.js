/* These are my functions */
$(document).ready(function() {
    //$(".cuscal-julian-date-converter").validate();

    doSomething();

    $(".entered-date").click(function() {
        $(this).val("");
    });

    $(".calculate").click(function() {
        var convertedDate;
        if ($("#ddmmyyyy").attr("checked")) {
            convertedDate = makeDate($(".entered-date").val());
            var theMonth = convertedDate.getMonth() + 1;
            theMonth = toString(theMonth);
            if (theMonth < 10) {
                theMonth = "0" + theMonth;
            }
            convertedDate = convertedDate.getDate() + "/" + theMonth + "/" + convertedDate.getFullYear();
        } else {
            convertedDate = makeJulian($(".entered-date").val());
        }

        $(".result-date").html(convertedDate);
    });
});

function makeJulian(enteredDate) {
    var nums = enteredDate.split("/");
    nums[1] = toInt(nums[1]);
    var theDate = new Date(nums[2], (nums[1] - 1), nums[0]);
    //var theDate = new Date(enteredDate);
    var dayOfYear = getDayOfYear(theDate);
    var year = theDate.getFullYear();
    //Convert dayOfYear and newYear to Strings
    dayOfYear = toString(dayOfYear);
    year = toString(year);
    if (dayOfYear < 10) {
        dayOfYear = "00" + dayOfYear;
    } else if(dayOfYear < 100) {
        dayOfYear = "0" + dayOfYear;
    }
    return (year.substring(2) + dayOfYear);
}

function getDayOfYear(dateOfYear) {
    var start = new Date(dateOfYear.getFullYear(),0,1);
    return Math.round(((dateOfYear - start)/86400000) + .5, 0);

    //Add one since the year has days between 0-364 and not 1-365 and calculate the day based on miliseconds.
    //return Math.ceil(((dateOfYear - start)/86400000));
}

function makeDate(julianNumber) {
    var year = julianNumber.substring(0,2);
    var dayOfYear = julianNumber.substring(2);
    var century = new Date().getFullYear();
    century = toString(century);
    century = century.substring(0,2);
    var fullYear = century + year;
    return getDateOfYear(dayOfYear, fullYear);
}

function getDateOfYear(dayOfYear, year){
    dayOfYear = toInt(dayOfYear);
    year = toInt(year);
    //Subtract one since we added 1 to the date before.
    dayOfYear = (dayOfYear == 0) ? dayOfYear : (dayOfYear - 1);
    var tempDate = new Date(year,0,1);
    tempDate.setDate(tempDate.getDate() + dayOfYear);
    return tempDate;
}

function toString(num) {
    return (num + '');
}

function toInt(string) {
    return (parseInt(string,10));
}

function doSomething() {
    if ($("#ddmmyyyy").attr("checked")) {
        $(".entered-date").val("Julian number");
    } else {
        $(".entered-date").val("dd/mm/yyyy");
    }
}