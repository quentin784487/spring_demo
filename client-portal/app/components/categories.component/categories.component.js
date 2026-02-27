(function () {
  'use strict';

  angular
    .module('myApp')
    .component('categoriesComponent', {
      templateUrl: 'app/components/categories.component/categories.component.html',
      bindings: {
        genres: '<',
        onSelect: '&'
      },
      controller: function () {
        var vm = this;
        
        vm.select = function (genre) {
          vm.onSelect({ genre: genre });
        };
      }
    });
})();