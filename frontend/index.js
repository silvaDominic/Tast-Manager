$(document).ready(function() {
    $('.dropdate').dropdate({
        dateFormat: 'mm/dd/yyyy'
    });

    var request;

    // Binds submit event from form
    $("task_form").submit(function(event){

        $form = $(this);

        // Aborts any pending requests
        if (request) {
            request.abort();
        }

        // Selects and caches input field
        var $inputs = $form.find("input");

        // Serializes form data
        var serializedData = $form.serialize();

        // Briefly disables input fields during duration of AJAX request
        $inputs.prop("disabled", true);

        // AJAX request
        request = $.ajax({
            url: "localhost:4567/tasks",
            type: "post",
            data: serializedData
        });
        // Callback handler for successful requests
        request.done(function (response, textStatus, jqXHR){
            console.log("Success")
        });

        // Callback handler for failed requests
        request.fail(function(jqXHR, textStatus, errorThrown){
            console.log("Error: " + textStatus, errorThrown)
        });

        // Reenable input fields
        request.always(function(){
            $inputs.prop("disabled", false);
        });
    })
});