/********************* 规则*******************************************************/

var ruleJson;
var ruleData = [];
var ruleDelId;
var ruleDel;
var ruleEditId;
var ruleResultData;
var dataSourceConfigList;
initRuleList();
$('.add_rule').on('click',function () { //点击添加按钮
    $('.rule_mask').show();
    document.getElementById('ruleForm').reset();
    $('.rule_mask_submit').val('insert');
    $('.rule_mask').find('em').text('');
    $('.select').find('input').val('');
    ruleEditId = null;
    //初始化页面
    initSaveHtml();
});
$('.rule_mask_cancel').on('click',function () { //点击取消按钮
    $('.rule_mask').hide();
});
$('.rule_mask_submit').on('click',function () {//点击提交
    ruleSubmitVal();
    var notice = $('.rule_type_center select').find('option:selected').val();
    $('.select').each(function () {
        var val = $(this).find('input').val();
        if(val == ''){
            $(this).find($('.sql_tips')).text('值不能为空');
            alert('请输入数据来源的sql');
            return false;
        }
    });
    if(notice =='javaExpress'){
        if($('.javaExpress_input input').val() == ''){
            $('.javaExpress_tips').text('该值不能为空');
            return false;
        }
    }else if(notice =='sqlResult'){
        if($('.sqlResult_show input').val()==''){
            $('.sqlResult_tips').text('该值不能为空');
            return false;
        }
    }
    if($('.rule_mask input[name=ruleCode]').val() == ''){
        $('.ruleCode_tips').text('ruleCode不能为空');
    }else if($('.rule_mask input[name=scheduleExpress]').val() == ''){
        $('.schedule_tips').text('scheduleExpress不能为空');
    }else if($('.rule_mask_content textarea').val() == ''){
        $('.text_tips').text('ruleExpress不能为空');
    } else {
        $.ajax({
            type:'post',
            contentType:'application/json',
            url:'/sdNoticeRule/save',
            dataType:'json',
            data:ruleJson,
            success:function (data) {
                console.log(data);
                if(data.code == 0){
                    // alert('提交成功');
                    $('.rule_mask').hide();
                    initRuleList();
                }else{
                    alert(data.message);
                }
            }
        });
    }
});


//初始化添加、编辑页面
function initSaveHtml(){
    //获取数据库列表
    var dataSourceConfigList = getDataSourceConfigList();
    if(dataSourceConfigList){
        $('.mask_select').empty();
        //创建一个数据源选择组件
        createSqlContentCpt('','',dataSourceConfigList);
    }
    //清除readonly
    $('.rule_mask').find('input[name=ruleCode]').prop("readonly",false);

}

function initRuleList() {//规则页面初始化
    var ruleObj = {};
    var ruleObjJson = JSON.stringify(ruleObj);
    $.ajax({
        type:'post',
        contentType:'application/json',
        url:'/sdNoticeRule/selectList',
        dataType:'json',
        data:ruleObjJson,
        success:function (data) {
            if(data.code == 0 && data.result){
                ruleData = data.result;
                for(var i=0;i<ruleData.length;i++){
                    ruleResultData = ruleData[i].sdSqlContentList;
                }
                initRuleData();
            }
        }
    })
}

//获取数据库列表
function getDataSourceConfigList() {
//    if(dataSourceConfigList){
//        return dataSourceConfigList;
//    }
    var dataSourceList = null;
    $.ajax({
        type:'post',
        contentType:'application/json',
        url:'/sdDataSourceConfig/selectList',
        dateType:'json',
        data:'{}',
        async: false,
        success:function (data) {
            console.log(data);
            var res = JSON.parse(data);
            if(res.code == 0){
                //返回结果
                dataSourceList = res.result;
            }
        }
    });
    dataSourceConfigList = dataSourceList;
    return dataSourceList;
}

//默认加载一次最新的数据源列表
function createSqlContentCpt(seletedValue,inputValue,dataSourceList) { //提交数据后刷新页面生成新的table
    if(!dataSourceList){
        return;
    }
    //初始化数据库选择框
    var selectContent = '<div class="select"><span>dataSource:</span> <select name="sqlOption">';
    for (var i = 0; i < dataSourceList.length; i++) {
       var option;
        if(seletedValue && seletedValue==dataSourceList[i].code){
            option = '<option selected="selected">' + dataSourceList[i].code + '</option>';
        }else{
            option = '<option>' + dataSourceList[i].code + '</option>';
        }
        selectContent += option;
    }
    selectContent += '</select><p><span>sql:</span><input name="sqlContent" type="text" value="'+inputValue+'"/><button class="sql_del" type="button">删除</button><em class="sql_tips"></em></p><div class="clearFix"></div></div>';
    $('.mask_select').append(selectContent);
    maskSqlDel();
}

//添加sqlContent
maskSqlDel();
$('.sql_add button').on('click',function () {
    //增加一个SQL内容组件
    createSqlContentCpt('','',dataSourceConfigList);
    $('.select input').unbind('blur').bind('blur',function () {
        if($(this).val() == ''){
            $(this).parent().find($('.sql_tips')).text('值不能为空');
        }else {
            $(this).parent().find($('.sql_tips')).text('');
        }
    });
});
function maskSqlDel() { //删除sqlContent
    $('.sql_del').unbind('click').bind('click',function () { //删除
        $(this).parent().parent().remove();
    });
};

//构建提交数据
function ruleSubmitVal() { //规则的数据收集
    var code = $('.rule_mask').find('input[name=ruleCode]').val();
    var name = $('.rule_mask').find('input[name=ruleName]').val();
    var express = $('.rule_mask').find('textarea[name=ruleExpress]').val();
    var schedule = $('.rule_mask').find('input[name=scheduleExpress]').val();
    var mail = $('.rule_mask').find('input[name=receiverMail]').val();
    var mobile = $('.rule_mask').find('input[name=receiverMobile]').val();
    var isEnable =  $('.radio_btn').find('input:checked').val();
    //获取sqlContent
    var sqlArr = [];
    $('.mask_select select').each(function () {
        var selectVal = $(this).find('option:selected').text();
        var inputVal = $(this).parent().find('input').val();
        sqlArr.push({'dataSourceCode':selectVal,'sqlContent':inputVal});
    });
    var ruleStr;
    //拼接通知内容
    var showSelect = $('.sqlResult_show select').find('option:selected').text();
    var showVal = $('.sqlResult_show input').val();
    var selectedVal = $('.rule_type_center select option:selected').val();
    var noticeContentType = $('.rule_type_center select option:selected').text();
    var noticeContent;
    if(noticeContentType == 'javaExpress'){
        //表达式通知内容
        var content = $('.javaExpress_input').find('input').val();
        noticeContent = {'type':noticeContentType,'noticeContent':content};
    }else {
        //SQL结果通知内容
        noticeContent = {'type':noticeContentType,'dataSourceCode':showSelect,'noticeContent':showVal};
    }
    ruleStr = {
        'id':ruleEditId,
        'ruleCode':code,
        'ruleName':name,
        'ruleExpress':express,
        'scheduleExpress':schedule,
        'receiverMail':mail,
        'receiverMobile':mobile,
        'noticeContent':noticeContent,
        'sdSqlContentList':sqlArr,
        'noticeContent':noticeContent,
        'isEnable': isEnable
    };
    ruleJson = JSON.stringify(ruleStr);
};
//$('.rule_type_center select').on('change',function () {
//    console.log($(this).val())
//    if($(this).val() == 'sqlResult'){
//        for(var i=0;i<ruleData.length;i++){
//            var result = ruleData[i].sdSqlContentList;
//            $('.sqlResult_show select').empty();
//            for(var j=0;j<result.length;j++){
//                $('.sqlResult_show select').append('<option>'+result[j].dataSourceCode+'</option>');
//            }
//        }
//    }
//});
function initRuleData() {
    var headList = ['ruleCode','ruleName','receiverMobile','isEnable','cronExpress','gmtCreated','gmtModified','操作'];
    $('.rule_head tr').empty();
    for(var i=0;i<headList.length;i++){
        $('.rule_head tr').append('<th>'+headList[i]+'</th>');
    };

    $('.rule_list tbody').empty();
    for(var j=0;j<ruleData.length;j++){
        var tdContent = '';
        for(var k=0;k<ruleData[j].sdSqlContentList.length;k++){
            // tdContent+='<td>'+ruleData[j].sdSqlContentList[k]+'</td>'
            $('select[name=sqlOption]').append('<option value="'+ruleData[j].sdSqlContentList[k].dataSourceCode+'">'+ruleData[j].sdSqlContentList[k].dataSourceCode+'</option>');
            // $('input[name=sqlContent]').val(ruleData[j].sdSqlContentList[k].sqlContent)
        }
        $('.rule_list tbody').append('<tr>' +
            '<td>'+ruleData[j].ruleCode+'</td>' +
            '<td>'+ruleData[j].ruleName+'</td>' +
//            '<td>'+ruleData[j].noticeContentType+'</td>' +
            '<td class="td_mobile">'+ruleData[j].receiverMobile+'</td>' +
            // '<td>'+ruleData[j].noticeContent+'</td>' +
            '<td>'+ruleData[j].isEnable+'</td>' +
            '<td>'+ruleData[j].scheduleExpress+'</td>' +
            '<td>'+ruleData[j].gmtCreated+'</td>' +
            '<td>'+ruleData[j].gmtModified+'</td>' +
            '<td class="rule_list_btn">' +
            '<input type="hidden" value="'+ruleData[j].id+'"/>' +
            '<a href="javascript:;" class="rule_edit">编辑</button>' +
            '<a href="javascript:;" class="rule_del">删除</button>' +
            '</td>' +
            '</tr>');
    };
    $('.td_mobile').each(function () {
        if($(this).text() == 'undefined'){
            $(this).text('');
        }
    });
    $('.rule_edit').unbind('click').bind('click',function () { //编辑
        //编辑不能修改code
        $('.rule_mask').find('input[name=ruleCode]').prop("readonly",true);
        $('.rule_mask').show();
        ruleEditId = $(this).parent().find('input').val();
        $('.rule_mask_submit').val('update');
        $.ajax({
            type:'get',
            url:'/sdNoticeRule/get/'+ruleEditId,
            contentType:'application/json',
            dataType:'json',
            success:function (data) {
                if(data.code == 0){
                    var ruleRes = data.result;
                    $('.rule_mask').find('input[name=ruleCode]').val(ruleRes.ruleCode);
                    $('.rule_mask').find('input[name=ruleName]').val(ruleRes.ruleName);
                    $('.rule_mask').find('textarea[name=ruleExpress]').val(ruleRes.ruleExpress);
                    $('.rule_mask').find('input[name=scheduleExpress]').val(ruleRes.scheduleExpress);
                    $('.rule_mask').find('input[name=receiverMail]').val(ruleRes.receiverMail);
                    $('.rule_mask').find('input[name=receiverMobile]').val(ruleRes.receiverMobile);
                    //设置是否开启标记
                    if(ruleRes.isEnable && ruleRes.isEnable=='Y'){
                       $('.radio_btn').find('input[name=onOff][value=Y]').prop("checked",true);
                    }else{
                       $('.radio_btn').find('input[name=onOff][value=N]').prop("checked",true);
                    }
                    //先清空select
                    $('.mask_select').empty();
                    //初始化通知内容
                    var sqlContentList = ruleRes.sdSqlContentList;
                    //获取数据库列表
                    var dataSourceConfigList = getDataSourceConfigList();
                    if(sqlContentList && dataSourceConfigList){
                        for(var i=0;i<sqlContentList.length;i++){
                            //创建一个数据源选择组件
                            createSqlContentCpt(sqlContentList[i].dataSourceCode,sqlContentList[i].sqlContent,dataSourceConfigList);
                        }
                    }
                    //设置通知类型
                    var content = ruleRes.noticeContent;
                    if(content){
                        $('.rule_type_center select').val(content.type);
                        //设置通知内容
                        if(content.type == 'javaExpress'){
                            //表达式
                             $('.javaExpress_input').find('input').val(content.noticeContent);
                             //展示表达式通知内容框
                             $('.sqlResult_show').hide();
                             $('.javaExpress_input').show();
                        }else {
                            //sql结果
                            //初始化数据源组件
                            if(dataSourceConfigList){
                                $('.sqlResult_show select').empty();
                                for(var i=0;i<dataSourceConfigList.length;i++){
                                    $('.sqlResult_show select').append('<option>'+dataSourceConfigList[i].code+'</option>');
                                }
                            }
                            $('.sqlResult_show select').val(content.dataSourceCode);
                            $('.sqlResult_show input').val(content.noticeContent);
                            //展示SQL结果通知内容框
                            $('.sqlResult_show').show();
                            $('.javaExpress_input').hide();
                        }
                    }
                }
            }
        })
    });

    $('.rule_del').unbind('click').bind('click',function () { //删除
        ruleDelId = $(this).parent().find('input').val();
        ruleDel = $(this).parent().parent();
        $('.del_rule_mask').show();
    });
};
$('.rule_sure_btn').on('click',function () { //规则确定删除
    $.ajax({
        type:'get',
        contentType:'application/json',
        url: '/sdNoticeRule/delete/'+ruleDelId,
        dateType:'json',
        success:function (data) {
            var delRes = JSON.parse(data)
            if(delRes.code == 0){
                $('.del_rule_mask').hide();
                initRuleList();
            }
        }
    })

});
$('.rule_cancel_btn').on('click',function () { //规则取消删除
    $('.del_rule_mask').hide();
});

////验证
//$('.rule_mask input[name=ruleCode]').on('blur',function () {
//    if($(this).val()==''){
//        $('.ruleCode_tips').text('ruleCode不能为空');
//    }else {
//        $('.ruleCode_tips').text('');
//    }
//});
//
//
//$('.rule_mask input[name=scheduleExpress]').on('blur',function () {
//    if($(this).val()==''){
//        $('.schedule_tips').text('scheduleExpress不能为空');
//    }else {
//        $('.schedule_tips').text('');
//    }
//});
//
//$('.rule_mask input[name=sqlContent]').on('blur',function () {
//    if($(this).val()==''){
//        $(this).parent().find($('.sql_tips')).text('值不能为空');
//    }else {
//        $('.sql_tips').text('');
//    }
//});
$('.rule_mask input[name=receiverMobile]').on('change',function () {
    var phone = $(this).val();
    if (!(/^1[3456789]\d{9}$/.test(phone))) {
        $(this).parent().find($('.mobile_tips')).text('手机号码格式不对');
    }else {
        $('.mobile_tips').text('');
    }
});
//$('.rule_mask_content textarea').on('blur',function () {
//    if($(this).val() == ''){
//        $('.text_tips').text('ruleExpress不能为空');
//    }
//});
//$('.javaExpress_input input').on('blur',function () {
//    if($(this).val() == ''){
//        $('.javaExpress_tips').text('该值不能为空');
//    }else {
//        $('.javaExpress_tips').text('');
//    }
//});
//$('.sqlResult_show input').on('blur',function () {
//    if($(this).val() == ''){
//        $('.sqlResult_tips').text('该值不能为空');
//    }else {
//        $('.sqlResult_tips').text('');
//    }
//});

//添加input
$('select[name=noticeContentType]').on('change',function () {
    var val = $(this).val();
    if(val == 'sqlResult'){
      //初始化数据源组件
        if(dataSourceConfigList){
            $('.sqlResult_show select').empty();
            for(var i=0;i<dataSourceConfigList.length;i++){
                $('.sqlResult_show select').append('<option>'+dataSourceConfigList[i].code+'</option>');
            }
        }
        $('.sqlResult_show').show();
        $('.javaExpress_input').hide();
    }else {
        $('.sqlResult_show').hide();
        $('.javaExpress_input').show();
    }
});
