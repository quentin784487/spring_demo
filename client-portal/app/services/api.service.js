(function () {
  'use strict';

  angular
    .module('myApp')
    .service('ApiService', function ($http) {

      this.getItems = function () {
        return $http.get('https://jsonplaceholder.typicode.com/users');
      };

    });
})();