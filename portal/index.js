const baseUrl = 'http://localhost:8080';
const pageSize = 10;

var games = [];
var downloadURLs = [];
var downloadURLCount = 0;
var currentPage = 0;
var resultCount = 0;
var title = 'none';
var modalState = '';
var selectedGame = null;
var images = [];
var imageCount = 0;
var coverImage = '';
var publisherCount = 0;
var publishers = [];
var downloadLinkState = '';
var downloadLinkIndex = 0;

$(document).ready(function () {
    loadGames(currentPage, pageSize, title);

    $("#add-new-game").click(function () {
        modalState = 'add';
        loadLookups();
    });

    $("#download-add").click(function () {
        saveDownload();
    });

    $("#save").click(function () {
        if (!validateForm()) {
            $('#validation-message').prop('hidden', false);
        } else {

            const url = modalState == "edit" ? '/api/admin/games/' + $('#primary-key').text() : '/api/admin/games';

            $.ajax({
                url: baseUrl + url,
                method: modalState == 'edit' ? 'PUT' : 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    "title": $("#title").val(),
                    "description": $("#description").val(),
                    "releaseYear": $("#releaseYear").val(),
                    "developer": $("#developer").val(),
                    "publishers": publishers,
                    "status": $("#status option:selected").val(),
                    "coverImage": coverImage,
                    "genre": parseInt($("#genre option:selected").val()),
                    "platform": parseInt($("#platform option:selected").val()),
                    "downloads": downloadURLs,
                    "images": images
                }),
                success: function (res) {
                    loadGames(currentPage, pageSize, title);
                    $("#crud-modal").modal("hide");
                    clearForm();
                    $("#save-success-modal").modal("show");
                    console.log(res);
                },
                error: function (xhr) {
                    console.error(xhr.responseText);
                }
            });
        }
    });

    $(".close").click(function () {
        $('#validation-message').prop('hidden', true);
        clearForm();
        $("#crud-modal").modal("hide");
    });

    $("#search").click(function () {
        title = $('#title-filter').val();
        loadGames(currentPage, pageSize, $('#title-filter').val());
    });

    $("#reset-search").click(function () {
        $('#title-filter').val('');
        loadGames(currentPage, pageSize, 'none');
    });

    $('#fileInput').on('change', function () {
        const file = this.files[0];

        if (!file) return;

        const reader = new FileReader();

        reader.onload = function (e) {
            const base64 = e.target.result;

            images.push(
                {
                    "imageBase64": base64,
                    "fileName": file.name
                }
            );

            imageCount = imageCount + 1;

            $("#screenshot-list").append(
                "<li id=" + imageCount + " class='list-group-item'>" +
                "    <div class='d-flex justify-content-between'>" +
                "        <label onclick='previewScreenshot(this)' class='form-label align-self-center align-self-center screenshot-link'>" + file.name + "</label>" +
                "        <button onclick='deleteImage(this)' class='btn btn-danger btn-sm'>" +
                "            <i class='bi bi-trash'></i>" +
                "        </button>" +
                "    </div>" +
                "</li>"
            );

            $("#fileInput").val('');
        };

        reader.readAsDataURL(file);
    });

    $('#coverImageInput').on('change', function () {
        const file = this.files[0];

        if (!file) return;

        const reader = new FileReader();

        reader.onload = function (e) {
            coverImage = e.target.result;
            $("#coverImageLink").text('Upoaded Image');
        };

        reader.readAsDataURL(file);
    });

    $('#publisher').on('change', function () {
        const selectedValue = $(this).val();
        const selectedText = $(this).find('option:selected').text();

        if ($.inArray(selectedValue, publishers) == -1 && selectedValue !== 'intro') {
            publisherCount = publisherCount + 1;
            publishers.push(
                {
                    id: selectedValue,
                    name: selectedText
                }
            );

            $("#publisher-list").append(
                "<li id=" + publisherCount + " class='list-group-item'>" +
                "    <div class='d-flex justify-content-between'>" +
                "        <label class='form-label align-self-center'>" + selectedText + "</label>" +
                "        <button onclick='deletePublisher(this)' class='btn btn-danger btn-sm'>" +
                "            <i class='bi bi-trash'></i>" +
                "        </button>" +
                "    </div>" +
                "</li>"
            );
        }
    });

    function debounce(fn, delay) {
        let timer = null;
        return function (...args) {
            clearTimeout(timer);
            timer = setTimeout(() => fn.apply(this, args), delay);
        };
    }

    const publisherSearch = debounce(function () {
        const query = $(this).val();

        if (query.length < 2) return;

        getPublishers(query);
    }, 1000);

    $('#publisher-search').on('input', publisherSearch);

    const developerSearch = debounce(function () {
        const query = $(this).val();

        if (query.length < 2) return;

        getDevelopers(query);
    }, 1000);

    $('#developer-search').on('input', developerSearch);
});

function getPublishers(name, id) {
    $.ajax({
        url: 'http://localhost:8080/api/admin/publishers?name=' + name,
        success: function (data) {
            if (data.length == 0) {
                $("#publisher").html('<option selected value="intro">No publishers found...</option>');
            } else {
                $("#publisher").html('');
                $("#publisher").html('<option selected value="intro">Select available publisher(s)</option>');
                for (var i = 0; i < data.length; i++) {
                    $("#publisher").append(
                        "<option value=" + data[i].id + ">" + data[i].name + "</option>"
                    );
                }
                $("#publisher").focus();
            }

            if (id) {
                $("#publisher").val(id);
            }
        }
    });
}

function getDevelopers(name, id) {
    $.ajax({
        url: 'http://localhost:8080/api/admin/developers?name=' + name,
        success: function (data) {
            if (data.length == 0) {
                $("#developer").html('<option selected value="intro">No developers found...</option>');
            } else {
                $("#developer").html('');
                $("#developer").html('<option selected value="intro">Select available developer(s)</option>');
                for (var i = 0; i < data.length; i++) {
                    $("#developer").append(
                        "<option value=" + data[i].id + ">" + data[i].name + "</option>"
                    );
                }
                $("#developer").focus();
            }

            if (id) {
                $("#developer").val(id);
            }
        }
    });
}

function navigate(action) {
    if (action == 'next') {
        currentPage = currentPage + 1;
        loadGames(currentPage, pageSize, title);
    } else if (action == 'previous') {
        currentPage = currentPage - 1;
        loadGames(currentPage, pageSize, title);
    }
}

function loadLookups() {
    $.ajax({
        url: baseUrl + '/api/admin/games/all-lookups',
        method: 'GET',
        dataType: 'json',
        success: function (response) {

            $("#genre").html('<option selected value="intro">Select Genre</option>');
            for (var i = 0; i < response.genres.length; i++) {
                $("#genre").append(
                    "<option value=" + response.genres[i].id + ">" + response.genres[i].name + "</option>"
                );
            }

            $("#platform").html('<option selected value="intro">Select Platform</option>');
            for (var i = 0; i < response.platforms.length; i++) {
                $("#platform").append(
                    "<option value=" + response.platforms[i].id + ">" + response.platforms[i].name + "</option>"
                );
            }

            if (modalState == 'edit') {
                $('#genre').val(selectedGame.genres[0].id);
                $('#platform').val(selectedGame.platforms[0].id);
            }

            $("#crud-modal").modal("show");
            $("#modal-title").text("Add Game");
        },
        error: function (xhr, status, error) {
            console.error('Error:', status, error);
        }
    });
}

function deleteGame(id) {
    $.ajax({
        url: baseUrl + '/api/admin/games/' + id,
        method: 'DELETE',
        success: function (res) {
            loadGames(currentPage, pageSize, title);
            $("#confirm-modal").modal("hide");
            $("#delete-success-modal").modal("show");
        },
        error: function (xhr) {
            console.error(xhr.responseText);
        }
    });
}

function loadGames(page, size, title) {
    $.ajax({
        url: baseUrl + '/api/admin/games' + '?page=' + page + '&size=' + size + '&title=' + title,
        method: 'GET',
        dataType: 'json',
        success: function (response) {

            $('.pagination').html('');

            $('.pagination').append('<li class="page-item"><a id="previous" class="page-link" href="#">Previous</a></li>');

            for (var i = 1; i <= response.totalPages; i++) {
                $('.pagination').append('<li class="page-item"><a id=' + i + ' class="page-link" href="#">' + i + '</a></li>');
            }

            $('.pagination').append('<li class="page-item"><a id="next" class="page-link" href="#">Next</a></li>');

            $("#previous").click(function () {
                navigate('previous');
            });

            $("#next").click(function () {
                navigate('next');
            });

            $(".page-link").click(function (currentPage) {
                if (currentPage) {
                    loadGames(currentPage.currentTarget.id - 1, pageSize, title);
                }
            });

            if (response.first == true) {
                $('#previous').addClass('disabled-link');
            } else {
                $('#previous').removeClass('disabled-link');
            }

            if (response.last == true) {
                $('#next').addClass('disabled-link');
            } else {
                $('#next').removeClass('disabled-link');
            }

            games = response.content;
            resultCount = response.content.length;
            $("#table-body").html('');

            for (var i = 0; i < games.length; i++) {
                $("#table-body").append(
                    "<tr id='" + response.content[i].id + "'> " +
                    "   <td>" + response.content[i].title + "</td>" +
                    "   <td>" + games[i].description + "</td>" +
                    "   <td>" + games[i].releaseYear + "</td>" +
                    "   <td>" + games[i].developer.name + "</td>" +
                    "   <td>" + games[i].publishers[0].name + "</td>" +
                    "   <td>" + games[i].status + "</td>" +
                    "   <td>" +
                    "       <div class='dropdown'>" +
                    "           <button class='btn btn-secondary dropdown-toggle' type='button' data-bs-toggle='dropdown' aria-expanded='false'>" +
                    "               Actions" +
                    "           </button>" +
                    "           <ul class='dropdown-menu'>" +
                    "               <li><a class='dropdown-item edit-action' href='#'>Edit</a></li>" +
                    "               <li><a class='dropdown-item confirm-delete' href='#'>Delete</a></li>" +
                    "           </ul>" +
                    "       </div>" +
                    "   </td>" +
                    "</tr>"
                );
            }

            $(".edit-action").click(function (element) {
                modalState = 'edit';
                const id = $(element.target).parents('tr').attr('id');
                selectedGame = games.find(g => g.id === parseInt(id));
                $("#crud-modal").modal("show");
                populateModalForm(selectedGame);
            });

            $(".confirm-delete").click(function (element) {
                var title = $(element.target).parent().closest('tr').find('td').first().text();
                const id = $(element.target).parent().closest('tr').attr('id');
                $('#delete-id').text(id);
                $("#confirm-message").text("Are you sure you want to delete '" + title + "'?");
                $("#confirm-modal").modal("show");
            });

            $("#delete-action").click(function () {
                deleteGame($('#delete-id').text());
            });
        },
        error: function (xhr, status, error) {
            console.error('Error:', status, error);
            $('#result').text('Something went wrong.');
        }
    });
}

function populateModalForm(game) {
    $("#modal-title").text("Edit Game");
    $("#primary-key").text(game.id);
    $("#title").val(game.title);
    $("#description").val(game.description);
    $("#releaseYear").val(game.releaseYear);

    getDevelopers(game.developer.name, game.developer.id);

    getPublishers(game.publishers[0].name, game.publishers[0].id);

    for (var i = 0; i < game.publishers.length; i++) {
        publisherCount = publisherCount + 1;
        publishers.push(
            {
                id: game.publishers[i].id,
                name: game.publishers[i].name
            }
        );

        $("#publisher-list").append(
            "<li id=" + publisherCount + " class='list-group-item'>" +
            "    <div class='d-flex justify-content-between'>" +
            "        <label class='form-label align-self-center'>" + game.publishers[i].name + "</label>" +
            "        <button onclick='deletePublisher(this)' class='btn btn-danger btn-sm'>" +
            "            <i class='bi bi-trash'></i>" +
            "        </button>" +
            "    </div>" +
            "</li>"
        );
    }

    $("#coverImage").val(game.coverImage);
    coverImage = game.coverImage;
    if (coverImage) {
        $("#coverImageLink").text('Upoaded Image');
    }
    $("#download-item").val(game.downloadUrl);
    $('#status').val(game.status);

    $("#url-list").html('');
    $("#url-list").append('<li class="list-group-item text-center">Items</li>');
    downloadURLs = [];

    for (var i = 0; i < game.downloads.length; i++) {
        downloadURLCount = downloadURLCount + 1;
        downloadURLs.push(
            {
                "link": game.downloads[i].downloadUrl,
                "name": game.downloads[i].name,
                "type": game.downloads[i].type
            }
        );

        $("#link-table-body").append(
            "<tr id='" + downloadURLCount + "'> " +
            "   <td>" + game.downloads[i].type + "</td>" +
            "   <td>" + game.downloads[i].name + "</td>" +
            "   <td>" +
            "       <div class='dropdown'>" +
            "           <button class='btn btn-secondary dropdown-toggle' type='button' data-bs-toggle='dropdown' aria-expanded='false'>" +
            "               Actions" +
            "           </button>" +
            "           <ul class='dropdown-menu'>" +
            "               <li><a onclick=editDownloadLink(this) class='dropdown-item' href='#'>Edit</a></li>" +
            "               <li><a onclick='deleteDownloadLink(this)' class='dropdown-item' href='#'>Delete</a></li>" +
            "           </ul>" +
            "       </div>" +
            "   </td>" +
            "</tr>"
        );
    }

    $("#screenshot-list").html('');
    $("#screenshot-list").append('<li class="list-group-item text-center">Items</li>');

    for (var i = 0; i < game.images.length; i++) {

        images.push(
            {
                "imageBase64": game.images[i].image,
                "fileName": game.images[i].fileName
            }
        );

        imageCount = imageCount + 1;

        $("#screenshot-list").append(
            "<li id=" + imageCount + " class='list-group-item'>" +
            "    <div class='d-flex justify-content-between'>" +
            "        <label onclick='previewScreenshot(this)' class='form-label align-self-center screenshot-link'>" + game.images[i].fileName + "</label>" +
            "        <button onclick='deleteImage(this)' class='btn btn-danger btn-sm'>" +
            "            <i class='bi bi-trash'></i>" +
            "        </button>" +
            "    </div>" +
            "</li>"
        );
    }

    loadLookups();
}

function previewScreenshot(element) {
    const index = $(element).closest('li').attr('id');
    var image = images[index - 1];
    $("#preview-modal").modal("show");
    $("#screenshot-preview").attr('src', image.imageBase64);
}

function previewCoverImage() {
    if (coverImage) {
        $("#preview-modal").modal("show");
        $("#screenshot-preview").attr('src', coverImage);
    }
}

function saveDownload() {
    if ($("#download-url").val() != '' && $("#download-name").val() != '' && $("#download-type option:selected").val() != 'select') {
        if (downloadLinkState == 'edit') {
            downloadURLs[downloadLinkIndex].link = $("#download-url").val();
            downloadURLs[downloadLinkIndex].name = $("#download-name").val();
            downloadURLs[downloadLinkIndex].type = $("#download-type").val();
            $("#link-table-body").html('');
            downloadURLCount = 0;

            for (var i = 0; i < downloadURLs.length; i++) {
                downloadURLCount = downloadURLCount + 1;

                $("#link-table-body").append(
                    "<tr id='" + downloadURLCount + "'> " +
                    "   <td>" + downloadURLs[i].type + "</td>" +
                    "   <td>" + downloadURLs[i].name + "</td>" +
                    "   <td>" +
                    "       <div class='dropdown'>" +
                    "           <button class='btn btn-secondary dropdown-toggle' type='button' data-bs-toggle='dropdown' aria-expanded='false'>" +
                    "               Actions" +
                    "           </button>" +
                    "           <ul class='dropdown-menu'>" +
                    "               <li><a onclick=editDownloadLink(this) class='dropdown-item' href='#'>Edit</a></li>" +
                    "               <li><a onclick='deleteDownloadLink(this)' class='dropdown-item' href='#'>Delete</a></li>" +
                    "           </ul>" +
                    "       </div>" +
                    "   </td>" +
                    "</tr>"
                );
            }
        } else {
            downloadLinkState = 'add';
            downloadURLCount = downloadURLCount + 1;
            downloadURLs.push(
                {
                    "link": $("#download-url").val(),
                    "name": $("#download-name").val(),
                    "type": $("#download-type").val()
                }
            );

            $("#link-table-body").append(
                "<tr id='" + downloadURLCount + "'> " +
                "   <td>" + $("#download-type option:selected").text() + "</td>" +
                "   <td>" + $("#download-name").val() + "</td>" +
                "   <td>" +
                "       <div class='dropdown'>" +
                "           <button class='btn btn-secondary dropdown-toggle' type='button' data-bs-toggle='dropdown' aria-expanded='false'>" +
                "               Actions" +
                "           </button>" +
                "           <ul class='dropdown-menu'>" +
                "               <li><a onclick=editDownloadLink(this) class='dropdown-item' href='#'>Edit</a></li>" +
                "               <li><a onclick='deleteDownloadLink(this)' class='dropdown-item' href='#'>Delete</a></li>" +
                "           </ul>" +
                "       </div>" +
                "   </td>" +
                "</tr>"
            );
        }

        $("#download-url").val('');
        $("#download-name").val('');
        $("#download-type").val('select');
    }
}

function deleteDownloadLink(element) {
    $(element).closest('tr').remove();
    const index = parseInt($(element).closest('tr').attr('id'));
    downloadURLs.splice(index - 1, 1);
    downloadURLCount = downloadURLCount - 1;
}

function editDownloadLink(element) {
    downloadLinkState = 'edit';
    const index = parseInt($(element).closest('tr').attr('id'));
    downloadLinkIndex = index - 1;
    $("#download-url").val(downloadURLs[index - 1].link);
    $("#download-name").val(downloadURLs[index - 1].name);
    $("#download-type").val(downloadURLs[index - 1].type);
    $("#download-url").focus();
}

function deletePublisher(element) {
    $(element).closest('li').remove();
    const index = parseInt($(element).closest('li').attr('id'));
    publishers.splice(index - 1, 1);
    publisherCount = publisherCount - 1;
}

function deleteImage(element) {
    $(element).closest('li').remove();
    const index = parseInt($(element).closest('li').attr('id'));
    images.splice(index - 1, 1);
    imageCount = imageCount - 1;
}

function clearForm() {
    $("#title").val(null);
    $("#description").val(null);
    $("#releaseYear").val(null);
    $("#developer").val(null);
    $("#publisher").val(null);
    $("#coverImageInput").val('');
    $("#download-item").val(null);
    $("#link-table-body").html('');
    $("#screenshot-list").html('');
    $("#screenshot-list").append('<li class="list-group-item text-center">Items</li>');
    $("#download-url").val('');
    $("#download-name").val('');
    $("#download-type").val('select');
    $('#status').val('select');
    $('#genre').val('intro');
    $('#platform').val('intro');
    $("#developer-search").val(null);
    $("#developer").html('');
    $("#developer").html('<option selected value="intro">Search developer name</option>');
    $("#publisher-search").val(null);
    $("#publisher").html('');
    $("#publisher").html('<option selected value="intro">Search publisher name</option>');
    $("#publisher-list").html('');
    $("#coverImageLink").text('Please select an image file.');

    downloadURLs = [];
    images = [];
    publishers = [];
    coverImage = '';
}

function validateForm() {
    if ($("#title").val() == '') {
        return false;
    }

    if ($("#description").val() == '') {
        return false;
    }

    if ($("#releaseYear").val() == '') {
        return false;
    }

    if ($("#developer").val() == '') {
        return false;
    }

    if (publishers.length < 1) {
        return false;
    }

    if (coverImage == '') {
        return false;
    }

    if (downloadURLs.length < 1) {
        return false;
    }

    if ($("#genre option:selected").val() == 'intro') {
        return false;
    }

    if ($("#platform option:selected").val() == 'intro') {
        return false;
    }

    if ($("#developer option:selected").val() == 'intro') {
        return false;
    }

    return true;
}

