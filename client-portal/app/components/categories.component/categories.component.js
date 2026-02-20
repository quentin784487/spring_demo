(function () {
  'use strict';

  angular
    .module('myApp')
    .component('categoriesComponent', {
      templateUrl: 'app/components/categories.component/categories.component.html',
      bindings: {
        title: '@'
      },
      controller: function () {
        var vm = this;

        vm.$onInit = function () {
          
        };
      }
    });
})();