(function () {
  'use strict';

  angular
    .module('myApp')
    .controller('MainController', function (ApiService) {

      var vm = this;

      vm.title = 'AngularJS + ui-router + API';
      vm.items = [];

      vm.loadData = function () {
        ApiService.getItems()
          .then(function (response) {
            vm.items = response.data;
          })
          .catch(function (error) {
            console.error('API error:', error);
          });
      };

    });
})();