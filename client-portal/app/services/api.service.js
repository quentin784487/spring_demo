(function () {
  'use strict';

  angular
    .module('myApp')
    .service('ApiService', function ($http) {

      const baseUrl = 'http://localhost:8080';

      this.getGames = function (page, size, title, method, genre) {
        return $http.get(baseUrl + '/api/public/games' + '?page=' + page + '&size=' + size + '&title=' + title + '&method=' + method + '&genre=' + genre);
      };

      this.getGenres = function () {
        return $http.get(baseUrl + '/api/public/genres');
      };

    });
})();