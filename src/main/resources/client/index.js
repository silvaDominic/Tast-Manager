$(document).ready(function() {
    $('.dropdate').dropdate({
        dateFormat:"UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
    });

    // Cache fields
    var $tasks = $('#tasks');
    var $target_date = $('.target_date');
    var $task_container = $('.task_container')

// ------------------------------------------- AJAX REQUESTS -----------------------------------------------------------
    // Variables for GET AJAX request
    var allTasksURL = '/tasks';
    var httpReq = 'GET';
    var doneLogic = function(response){
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
    }

    // AJAX call for GETTING current tasks
    ajaxCall(allTasksURL, httpReq, 'undefined', doneLogic);

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

        // Aborts any pending requests
        if (request) {
            request.abort();
        }
        // Selects and caches input field
        var $inputs = $form.find("input");

        // Briefly disables input fields during duration of AJAX request
        $inputs.prop("disabled", true);

        // Variables for POST AJAX request
        var postTaskURL = $form.attr('action');
        var httpReq = $form.attr('method');
        var doneLogic = function(response){
            insertTask($.parseJSON(response), function(){
                $tasks.find($('.dropdate')).dropdate({
                    dateFormat:"UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
                    });
            });
        }
        // Reenable inputs after every request
        var alwaysLogic = function(){
            $inputs.prop("disabled", false);
        }

        // AJAX request for POSTING new task
        ajaxCall(postTaskURL, httpReq, taskToPost, doneLogic, 'undefined', alwaysLogic);
    });

    // Handler for DELETING selected tasks
    $tasks.delegate('.remove', 'click', function() {
        // Variables for DELETE AJAX request
        var taskURL = '/tasks/' + $(this).attr('data-id');
        var httpReq = 'DELETE';
        var $taskToDelete = $(this).closest('li');
        var doneLogic = function(){
            // Remove from DOM
            $taskToDelete.remove();
        }
        // AJAX call for DELETING current tasks
        ajaxCall(taskURL, httpReq, 'undefined', doneLogic);
    });

    // Handler for DELETING ALL tasks
    $('#delete_all').on('click', function(){
        var allTasksURL = '/tasks';
        var httpReq = 'DELETE';
        // AJAX call for DELETING current tasks
        ajaxCall(allTasksURL, httpReq);
        $('ul > li').each(function(){
            $(this).remove();
        })
    });

    // Updates task status
    $tasks.delegate('.status', 'click', function(){
        // Variables for PUT AJAX request
        var taskURL = '/tasks/' + $(this).attr('data-id');
        var httpReq = 'PUT';
        var task_to_update = JSON.stringify($(this).serializeData());
        // AJAX call for UPDATING status of current task
        ajaxCall(taskURL, httpReq, task_to_update);
    });

    // Enables editing of existing task
    $tasks.delegate('.update_form_container', 'click', function(event){
        // Cache fields
        var $task_description = $(this).find('.task_description');
        var $target_date = $(this).find('.target_date');
        // Enable fields
        $task_description.prop('disabled', false);
        $target_date.prop('disabled', false);
        // Prevents multiple clicks
        $(this).off(event);

        // Update task if changed
        $(this).change(function(){
            // Variables for PUT AJAX request
            var taskURL = $tasks.find($('.update_form')).attr('action') + $(this).find('#task').attr('data-id');
            var httpReq = $tasks.find($('.update_form')).attr('method');
            var $updated_form = JSON.stringify($(this).find('.update_form').serializeData());
            var doneLogic = function(){
               $task_description.prop('disabled', true);
               $target_date.prop('disabled', true);
            }
            // AJAX call for UPDATING task
            ajaxCall(taskURL, httpReq, $updated_form, doneLogic);
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

    function ajaxCall(_url, _type, _data, doneHelperFunction, failHelpFunction, alwaysHelperFunction){
        $.ajax({
            url: _url,
            type: _type,
            data: _data
        }).done(function(response, textStatus, jqXHR){
            if (typeof(doneHelperFunction) != 'undefined'){
                doneHelperFunction(response);
            }
            console.log(_type + " successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).fail(function(jqXHR, textStatus, errorThrown){
            if (typeof(failHelperFunction) != 'undefined'){
                failHelperFunction(response);
            }
            console.log("Error.");
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).always(function(){
            if (typeof(alwaysHelperFunction) != 'undefined'){
                alwaysHelperFunction();
            }
        });
    }
});