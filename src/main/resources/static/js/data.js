/********************* 数据源*******************************************************/
$('.add_data').on('click',function () { //点击添加按钮
    $('.data_mask').show();
    $('.data_mask_submit').val('data_add');
    //初始化id
    $('.data_mask').find('input[name=id]').val("");
    document.getElementById('dataForm').reset();
    //清除readonly
    $('.data_mask').find('input[name=code]').prop("readonly",false);
});
$('.data_mask_cancel').on('click',function () { //点击取消按钮
    $('.data_mask').hide();
});
var dataJson;
var dataList;
var delDataId;
initDataList(); //页面初始化数据

$('.data_mask_submit').on('click',function () {//点击数据源的提交
    dataSubmit();
    console.log(dataJson);
    var id = $('.data_mask').find('input[name=id]').val();
    //默认insert
    var method = "insert";
    if(id){
        //如果id不为空，则为更新
        method = "update";
    };
    if($('.data_mask input[name=code]').val() == ''){
        $('.code_tips').text('code不能为空');
    }else if($('.data_mask input[name=driverClass]').val() == ''){
        $('.driver_tips').text('driverClass不能为空');
    }else if($('.data_mask input[name=user]').val() == ''){
        $('.user_tips').text('user不能为空');
    }else if($('.data_mask input[name=password]').val() == ''){
        $('.pass_tips').text('password不能为空');
    }else if($('.data_mask input[name=jdbcUrl]').val() == ''){
        $('.url_tips').text('jdbcUrl不能为空');
    }else {
        $.ajax({
            type:'post',
            contentType:'application/json',
            url:'/sdDataSourceConfig/save',
            dateType:'json',
            data:dataJson,
            success:function (data) {
                var status = JSON.parse(data);
                console.log(status);
                if(status.code == 0){
                    alert('保存成功');
                    dataListQuery();
                    $('.data_mask').hide();
                }else {
                    alert(data.message);
                }

            }
        });
    }
});

function initDataList() { //初始化数据源列表展示
    var jsonStr = {};
    var jsonObj = JSON.stringify(jsonStr);
    $.ajax({
        type:'post',
        contentType:'application/json',
        url:'/sdDataSourceConfig/selectList',
        dateType:'json',
        data:jsonObj,
        success:function (data) {
            var resultData = JSON.parse(data);
            console.log(resultData);
            if(resultData.code == 0){
                dataList = resultData.result;
                createList();
            }
        }
    })
};
function dataListQuery() { //提交数据后刷新页面生成新的table
    dataSubmit();
    console.log(dataJson);
    $.ajax({
        type:'post',
        contentType:'application/json',
        url:'/sdDataSourceConfig/selectList',
        dateType:'json',
        data:dataJson,
        success:function (data) {
            console.log(data);
            var res = JSON.parse(data);
            if(res.code == 0){
                // window.location.reload();
                initDataList();
            }
        }
    })
}
function dataSubmit(){  //收集数据源的数据
    var id = $('.data_mask').find('input[name=id]').val();
    var code = $('.data_mask').find('input[name=code]').val();
    var driver = $('.data_mask').find('input[name=driverClass]').val();
    var user = $('.data_mask').find('input[name=user]').val();
    var pass = $('.data_mask').find('input[name=password]').val();
    var min = $('.data_mask').find('input[name=minPoolSize]').val();
    var max = $('.data_mask').find('input[name=maxPoolSize]').val();
    var time = $('.data_mask').find('input[name=maxIdleTime]').val();
    var idle = $('.data_mask').find('input[name=idleConnectionTestPeriod]').val();
    var url = $('.data_mask').find('input[name=jdbcUrl]').val();
    var dataStr = {
        'id':id,
        'code':code,
        'driverClass':driver,
        'jdbcUrl':url,
        'user':user,
        'password':pass,
        'minPoolSize':min,
        'maxPoolSize':max,
        'maxIdleTime':time,
        'idleConnectionTestPeriod':idle
    };
    dataJson = JSON.stringify(dataStr);
}

function createList() { //创建数据源的table
    var headList = ['user','code','driverClass','jdbcUrl','minPoolSize','maxPoolSize','maxIdleTime','idleConnectionTestPeriod','操作'];
    $('.data_head tr').empty();
    for(var i=0;i<headList.length;i++){
        $('.data_head tr').append('<th>'+headList[i]+'</th>');
    };
    $('.data_list tbody').empty();
    if(dataList){
        for(var j=0;j<dataList.length;j++){
            $('.data_list tbody').append('<tr>' +
                '<td>'+dataList[j].user+'</td>' +
                '<td>'+dataList[j].code+'</td>' +
                '<td>'+dataList[j].driverClass+'</td>' +
                '<td>'+dataList[j].jdbcUrl+'</td>' +
                '<td class="td_min">'+dataList[j].minPoolSize+'</td>' +
                '<td class="td_max">'+dataList[j].maxPoolSize+'</td>' +
                '<td class="td_time">'+dataList[j].maxIdleTime+'</td>' +
                '<td class="td_idle">'+dataList[j].idleConnectionTestPeriod+'</td>' +
                '<td class="data_list_btn">' +
                '<input type="hidden" value="'+dataList[j].id+'"/>' +
                '<a href="javascript:;" class="data_edit">编辑</a>' +
                '<a href="javascript:;" class="data_del">删除</a>' +
                '</td>' +
                '</tr>');
        };
    }
    $('.td_min').each(function () {
        if($(this).text() == 'undefined')
            $(this).text('');
    });
    $('.td_max').each(function () {
        if($(this).text() == 'undefined')
            $(this).text('');
    });
    $('.td_time').each(function () {
        if($(this).text() == 'undefined')
            $(this).text('');
    });
    $('.td_idle').each(function () {
        if($(this).text() == 'undefined')
            $(this).text('');
    });
    $('.data_edit').unbind('click').bind('click',function () { //编辑
        //编辑不能修改code
        $('.data_mask').find('input[name=code]').prop("readonly",true);
        var editDataId = $(this).parent().find('input').val();
        $('.data_mask_submit').val('dataEdit');
        $('.data_mask').show();
        $.ajax({
            type:'get',
            contentType:'application/json',
            url:'/sdDataSourceConfig/get/'+editDataId,
            dataType:'json',
            success:function (data) {
                console.log(data)
                if(data.code==0){
                    var editData = data.result;
                    $('.data_mask').find('input[name=code]').val(editData.code);
                    $('.data_mask').find('input[name=driverClass]').val(editData.driverClass);
                    $('.data_mask').find('input[name=user]').val(editData.user);
                    $('.data_mask').find('input[name=password]').val(editData.password);
                    $('.data_mask').find('input[name=minPoolSize]').val(editData.minPoolSize);
                    $('.data_mask').find('input[name=maxPoolSize]').val(editData.maxPoolSize);
                    $('.data_mask').find('input[name=maxIdleTime]').val(editData.maxIdleTime);
                    $('.data_mask').find('input[name=idleConnectionTestPeriod]').val(editData.idleConnectionTestPeriod);
                    $('.data_mask').find('input[name=jdbcUrl]').val(editData.jdbcUrl);
                    $('.data_mask').find('input[name=id]').val(editData.id);
                }
            }
        });
    });

    $('.data_del').unbind('click').bind('click',function () { //删除
        delDataId = $(this).parent().find('input').val();
        $('.del_mask').show();
    });

};
// 确定删除
$('.sure_btn').on('click',function(){
    $.ajax({
        type:'get',
        contentType:'application/json',
        url:'/sdDataSourceConfig/delete/'+delDataId,
        dateType:'json',
        success:function (data) {
            var status = JSON.parse(data);
            if(status.code == 0){
                dataListQuery();
                $('.del_mask').hide();
            }else {
                alert(data.message);
            }
        }
    })

});
// 取消删除
$('.cancel_btn').on('click',function(){
    $('.del_mask').hide();
});
//验证必填项
$('.data_mask input[name=code]').on('blur',function () {
    if($(this).val() == ''){
        $('.code_tips').text('code不能为空');
    }else {
        $('.code_tips').text('') ;
    }
});
$('.data_mask input[name=driverClass]').on('blur',function () {
    if($(this).val() == ''){
        $('.driver_tips').text('driverClass不能为空');
    }else {
        $('.driver_tips').text('') ;
    }
});
$('.data_mask input[name=user]').on('blur',function () {
    if($(this).val() == ''){
        $('.user_tips').text('user不能为空');
    }else {
        $('.user_tips').text('') ;
    }
});
$('.data_mask input[name=password]').on('blur',function () {
    if($(this).val() == ''){
        $('.pass_tips').text('password不能为空');
    }else {
        $('.pass_tips').text('') ;
    }
});
$('.data_mask input[name=jdbcUrl]').on('blur',function () {
    if($(this).val() == ''){
        $('.url_tips').text('jdbcUrl不能为空');
    }else {
        $('.url_tips').text('') ;
    }
});

