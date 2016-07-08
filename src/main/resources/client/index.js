$(document).ready(function() {
    $('.dropdate').dropdate({
        dateFormat:"UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
    });

    // Cache fields
    var $tasks = $('#tasks');
    var $target_date = $('.target_date');
    var $task_container = $('.task_container')

// ------------------------------------------- AJAX REQUESTS -----------------------------------------------------------

    // Handler for submitting form data
    // Binds submit event from form
    $("#task_form").submit(function(event){

        var request;
        var task_input = document.forms['task_form']['task'].value;

        // Check for blank form
        if (task_input == null || task_input == ""){
            alert("No task? Then I guess you don't need me!");
            return false;
        }

        // Prevent form from submitting from browser
        event.preventDefault();

        // Serialize form data
        var $form = $(this);
        var taskToPost = JSON.stringify($form.serializeData());
        console.log("Task to post:");
        console.log(taskToPost);

        // Aborts any pending requests
        if (request) {
            request.abort();
        }
        // Selects and caches input field
        var $inputs = $form.find("input");

        // Briefly disables input fields during duration of AJAX request
        $inputs.prop("disabled", true);

        // AJAX request for posting new task
        $.ajax({
            url: $form.attr('action'),
            type: $form.attr('method'),
            data: taskToPost
        }).done(function(response, textStatus, jqXHR){
            insertTask($.parseJSON(response), function(){
                $tasks.find($('.dropdate')).dropdate({
                    dateFormat:"UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
                    });
            });
            console.log("Post successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).fail(function(jqXHR, textStatus, errorThrown){
            console.log("Error.");
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).always(function(){
            $inputs.prop("disabled", false);;

        });
    });

    // AJAX call for getting current tasks
    $.ajax({
        url: '/tasks',
        type: 'GET'
    }).done(function(response, textStatus, jqXHR){
        $.each($.parseJSON(response), function(i, task){
            insertTask(task, function(){
                if (task.status == true){
                    $tasks.find($('#' + task.id)).prop('checked', true);
                    console.log(task);
                }
            $tasks.find($('.dropdate')).dropdate({
                dateFormat:"UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
                });
            });
        });
        console.log("Get successful.");
        console.log("Response: " + response);
        console.log("Text Status: " + textStatus);
        console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
    }).fail(function(jqXHR, textStatus, errorThrown){
        console.log("Error.");
        console.log("Text Status: " + textStatus);
        console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
    });

    // Handler for deleting selected tasks
    $tasks.delegate('.remove', 'click', function() {
        // Cache task to delete <li>
        var $taskToDelete = $(this).closest('li');

        // AJAX request for deleting existing task
        $.ajax({
            url: '/tasks/' + $(this).attr('data-id'),
            type: 'DELETE'
        }).done(function(response, textStatus, jqXHR){
            // Remove from DOM
            $taskToDelete.remove();
            console.log("Delete successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).fail(function(jqXHR, textStatus, errorThrown){
          console.log("Error.");
          console.log("Text Status: " + textStatus);
          console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        });
    });

    // Updates task status
    $tasks.delegate('.status', 'click', function(){
        var task_to_update = JSON.stringify($(this).serializeData());
        console.log(task_to_update);
        $.ajax({
            url: '/tasks/' + $(this).attr('data-id'),
            type: 'PUT',
            data: task_to_update
        }).done(function(response, textStatus, jqXHR){
            console.log("Put successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).fail(function(jqXHR, textStatus, errorThrown){
            console.log("Error.");
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        });
    });

    // Enable editing of task
    $tasks.delegate('.update_form_group', 'click', function(event){
        var $task_description = $(this).find('.task_description');
        var $target_date = $(this).find('.target_date');
        console.log($target_date);
        $task_description.prop('disabled', false);
        $target_date.prop('disabled', false);
        console.log("Clicked");
        $(this).off(event);
        $(this).change(function(){
            var $updated_form = JSON.stringify($(this).find('.update_form').serializeData());
            console.log($updated_form);
            $.ajax({
                url: '/tasks/' + $(this).find('#task').attr('data-id'),
                type: 'PUT',
                data: $updated_form
            }).done(function(response, textStatus, jqXHR){
               $task_description.prop('disabled', true);
               $target_date.prop('disabled', true);
                console.log("Put successful.");
                console.log("Response: " + response);
                console.log("Text Status: " + textStatus);
                console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
            }).fail(function(jqXHR, textStatus, errorThrown){
                console.log("Error.");
                console.log("Text Status: " + textStatus);
                console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
            });
        });
    });

// ---------------------------------------- HELPER FUNCTIONS -----------------------------------------------------------

    // Uses the mustache template engine to dynamically insert tasks into DOM
    function insertTask(data, callback){
        $.Mustache.load('templates/tasks.html', function() {
            $('#tasks').mustache('main_form', data);
            if (callback && typeof(callback) == 'function'){
                callback();
            }
        });
    }
});