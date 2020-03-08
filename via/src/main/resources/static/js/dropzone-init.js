Dropzone.autoDiscover = false;

var  dropzone = $("div#img-dropzone").dropzone({ url: "/via/form/file",
        capture: null,
        previewsContainer: "#img-dropzone",
        thumbnailMethod: "crop",
        maxFiles: 5,
        acceptedFiles: "image/*",
        dictMaxFilesExceeded: "Up to 5 photos allowed. 最多5張相片。",
        clickable: true,
        addRemoveLinks: true,
        init: function(){
            this.on("thumbnail", function(file, url) {
                console.log(file);
                file.previewElement.addEventListener("click", function() {
                    var regex = new RegExp("^data");
                    if(!regex.test(url)){
                        var win = window.open(url, '_blank');
                        win.focus();
                    }
                });// will send to console all available props
            });

            this.on("sending", function(file) {
                //file.myCustomName = new Date().getTime() + file.name;
                console.log('upload file:' + file.upload.filename);
                console.log(file.myCustomName);
            });

            this.on("addedfile", function(file) {

                if(file.previewElement.baseURI.includes("edit-form")){
                    //this.options.maxFiles = this.options.maxFiles - 1;
                    console.log(this.options.maxFiles);
                    if(this.files.length > this.options.maxFiles){
                        this.removeFile(file);
                        $('#up-to-five').modal('show');
                        console.log("delete photo");
                    }
                }



                if (this.files.length) {
                    var _i, _len;
                    for (_i = 0, _len = this.files.length; _i < _len - 1; _i++) // -1 to exclude current file
                    {
                        if(this.files[_i].name === file.name
                            && this.files[_i].size === file.size
                            && this.files[_i].lastModifiedDate.toString() === file.lastModifiedDate.toString())
                        {
                            this.removeFile(file);
                            $('#img-dup').modal('show');
                        }
                    }
                }

                var removeButton = Dropzone.createElement("<a href=\"#\">Open file</a>");
            });

            this.on("removedfile", function(file) {
                console.log(this.options.maxFiles);
                if(file.previewElement.baseURI.includes("edit-form")){
                    $.ajax({
                        type: 'POST', url: "/via/edit-form/file/remove",
                        data: {
                            name : file.name,
                            id : file.previewElement.id
                        },
                        success: function(result){
                            console.log("success");
                        }
                    });

                    //this.options.maxFiles = this.options.maxFiles + 1;
                    console.log(this.options.maxFiles);
                }
                    $.ajax({
                        type: 'POST',
                        url: "/via/form/file/remove",
                        data: {name : file.upload.filename},
                        success: function(result){
                            console.log("success");
                        }});
                console.log("test");
            });


            if(true){
                for(i = 0; i < mockFiles.length; i++){
                    var n = mockFiles[i].lastIndexOf('/');
                    var testFilethis = testFiles;
                    var nameResult = mockFiles[i].substring(n + 1);
                    var mockFile =  {name: nameResult, size: 20000};
                    this.emit("addedfile", mockFile);
                    this.emit("thumbnail", mockFile,  mockFiles[i]);
                    this.emit("complete", mockFile);
                    //this.options.maxFiles = this.options.maxFiles - 1;
                    this.files.push(mockFile);
                    $(".dz-preview:last-child").attr('id', testFilethis[i].id);
                }

            }

        },
        renameFile: function(file) {
            return new Date().getTime() + file.name;

        }



    }
);

$('#dz-details').on('click', function()
{
    console.log("filename :",$('#dz-filename').value());
});