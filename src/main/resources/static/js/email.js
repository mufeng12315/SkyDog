/***********************邮箱***************************************************/
var emailJson;
var myReg=/^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
initEmail();
$('.email_btn button').on('click',function () { //email保存
    emailSubmintVal();
    var eVal = $('.email_info input[name=mail]').val();
    if($('.email_info input[name=host]').val() == ''){
        $('.host_tips').text('host不能为空');
    }else if($('.email_info input[name=port]').val()==''){
        $('.port_tips').text('port不能为空');
    }else if($('.email_info input[name=username]').val()==''){
        $('.username_tips').text('username不能为空');
    }else if($('.email_info input[name=password]').val()==''){
        $('.e_pass_tips').text('password不能为空');
    }else if($('.email_info input[name=mail]').val()==''){
        $('.e_mail_tips').text('mail不能为空');
    }else if(!myReg.test(eVal)){
        $('.e_mail_tips').test('mail格式不对');
    } else {
        $.ajax({
            type:'post',
            contentType:'application/json',
            url: '/sdConfig/save',
            dateType:'json',
            data:emailJson,
            success:function (data) {
                console.log(data);
                var res = JSON.parse(data);
                if(res.code == 0){
                    alert('保存成功');
                }
            }
        })
    }
});
function initEmail() { //email页面初始化查询
    emailSubmintVal();
    $.ajax({
        type:'get',
        contentType:'application/json',
        url: '/sdConfig/get/senderMail',
        dateType:'json',
        success:function (data) {
            var emailData = JSON.parse(data);
            if(emailData.code == 0 && emailData.result &&
                        emailData.result.configValue){
                var emailRes = emailData.result.configValue;
                $('.email_info input[name=host]').val(emailRes.smtpHost);
                $('.email_info input[name=port]').val(emailRes.smtpPort);
                $('.email_info input[name=username]').val(emailRes.senderUsername);
                $('.email_info input[name=password]').val(emailRes.senderPassword);
                $('.email_info input[name=mail]').val(emailRes.senderMail);
                $('.email_info input[name=timeout]').val(emailRes.timeout);
                $('.email_btn button').val(emailData.result.id);
            }

        }
    });
}
function emailSubmintVal() {
    var host = $('.email_info input[name=host]').val();
    var port = $('.email_info input[name=port]').val();
    var user= $('.email_info input[name=username]').val();
    var word = $('.email_info input[name=password]').val();
    var mail = $('.email_info input[name=mail]').val();
    var time = $('.email_info input[name=timeout]').val();
    var emailId = $('.email_btn button').val();
    var senderMail = 'senderMail';
    var emailData;
    if(emailId != 'insert'){
        emailData = {
            'code':senderMail,
            'configValue':{
                'id':emailId,
                'smtpHost':host,
                'smtpPort':port,
                'senderUsername':user,
                'senderPassword':word,
                'senderMail':mail,
                'timeout':time
            }
        }

    }else {
        emailData = {
            'code':senderMail,
            'configValue':{
                'smtpHost':host,
                'smtpPort':port,
                'senderUsername':user,
                'senderPassword':word,
                'senderMail':mail,
                'timeout':time
            }
        }
    }

    emailJson = JSON.stringify(emailData);
    console.log(emailJson)
}
//验证
$('.email_info input[name=host]').on('blur',function () {
    if($(this).val() == ''){
        $(this).parent().find($('.host_tips')).text('host不能为空')
    }else {
        $('.host_tips').text('');
    }
});
$('.email_info input[name=port]').on('blur',function () {
    if($(this).val() == ''){
        $(this).parent().find($('.port_tips')).text('port不能为空')
    }else {
        $('.port_tips').text('');
    }
});
$('.email_info input[name=username]').on('blur',function () {
    if($(this).val() == ''){
        $(this).parent().find($('.username_tips')).text('username不能为空')
    }else {
        $('.username_tips').text('');
    }
});
$('.email_info input[name=password]').on('blur',function () {
    if($(this).val() == ''){
        $(this).parent().find($('.e_pass_tips')).text('password不能为空')
    }else {
        $('.e_pass_tips').text('');
    }
});
$('.email_info input[name=mail]').on('blur',function () {
    var val = $(this).val();
    console.log(val);
    if(!myReg.test(val)){
        $('.e_mail_tips').text('email格式不对');
    }else if(val == ''){
        $('.e_mail_tips').text('email格不能为空');
    }else {
        $('.e_mail_tips').text('');
    }
});