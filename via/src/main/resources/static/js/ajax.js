


function addRow () {
    var cloneIndex = $("#itemList #item").length;
    var firstId;
    var list = $("#itemList");
    var itemIndexsObj = $("input[name='item-index']");
    var indexList = [];

    if(cloneIndex >= 5){
        $('#up-to-five').modal('show');
        return;
    }

    for(i = 0; i < itemIndexsObj.length; i ++){
        indexList.push(itemIndexsObj[i].value);
    }

    if(indexList.length > 0){
        indexList.sort();
        cloneIndex = parseInt(indexList[indexList.length - 1]) + 1;
        firstId = indexList[0];
    }

    var itemNum = new RegExp("#" + (parseInt(firstId) + 1), "g");

    var itemName = new RegExp("itemName-" + firstId, "g");
    var formId = new RegExp("form-id-" + firstId, "g");
    var itemRemark = new RegExp("itemRemark-" + firstId, "g");
    var qty = new RegExp("qty-" + firstId, "g");
    var party = new RegExp("party-" + firstId, "g");
    var activity = new RegExp("activity-" + firstId, "g");
    var groupNum = new RegExp("groupNum-" + firstId, "g");

    var arrayIndex = new RegExp("\\[" + firstId + "\\]", "g");
    var htmlText  =document.getElementById("itemList").firstElementChild
        .outerHTML
        .replace(/value=".*"/g,"value=\"\"")
        .replace(/name="item-index" value=".*"/g,"name=\"item-index\" value=\"" + cloneIndex + "\"")
        .replace(itemName, "itemName-"+cloneIndex)
        .replace(formId, "form-id-"+cloneIndex)
        .replace(itemRemark, "itemRemark-"+cloneIndex)
        .replace(qty, "qty-"+cloneIndex)
        .replace(party, "party-"+cloneIndex)
        .replace(activity, "activity-"+cloneIndex)
        .replace(groupNum, "groupNum-"+cloneIndex)
        .replace(arrayIndex, "[" + cloneIndex + "]")
        .replace(itemNum, "#" + (cloneIndex + 1));

    list.append(htmlText);
    var newValue = document.getElementById("party-" + cloneIndex);
    var oldValue = document.getElementById("party-" + (cloneIndex - 1));
    newValue.value = oldValue.value;

    var newActivity = document.getElementById("activity-" + cloneIndex);
    var oldActivity = document.getElementById("activity-" + (cloneIndex - 1));
    document.getElementById("qty-" + cloneIndex).value = 1;

    newActivity.value = oldActivity.value;

}



function delRow (id) {
    var cloneIndex = $("#itemList #item").length;
    if(cloneIndex > 1)
        $(id).parent().parent().parent().parent().remove();
    else
        alert("Cannot delete");
}

function validateForm(){
    var countImg = $(".dz-filename").length;

    if(countImg === 0){
        $('#no-img').modal('show');
        return false;
    }

}

function userProfileValidate(){
    if ($('#firstPw').val() != $('#secPw').val()) {
        $('#no-img').modal('show');
        return false;
    }
}

function rowClicked(value){
    location.href = "/via/edit-form/edit-form?id=" + value;
}


$(function() {
    $('input[name="datetimes"]').daterangepicker({
        timePicker: false,
        startDate: moment().startOf('day'),
        endDate: moment().startOf('day').add(1, 'day'),
        locale: {
            format: 'DD/MM/YYYY'
        }
    });
})

$(function() {
    $('input[name="time"]').daterangepicker({
        timePicker: false,
        autoUpdateInput: true,
        locale: {
            format: 'DD/MM/YYYY'
        }
    });

    $('input[name="time"]').val("");
});

function  downloadForm(id) {
    location.href='/via/edit-form/export?id=' + id;
}

function addOrUpdateUrlParam(name, value)
{
    var href = window.location.href;
    var regex = new RegExp("[&\\?]" + name + "=");
    if(regex.test(href))
    {
        regex = new RegExp("([&\\?])" + name + "=[\\w\\d\\W]+");
        window.location.href = href.replace(regex, "$1" + name + "=" + value);
    }
    else
    {
        if(href.indexOf("?") > -1)
            window.location.href = href + "&" + name + "=" + value;
        else
            window.location.href = href + "?" + name + "=" + value;
    }
}

