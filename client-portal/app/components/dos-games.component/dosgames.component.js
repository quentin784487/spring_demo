(function () {
  'use strict';

  angular
    .module('myApp')
    .component('dosGamesComponent', {
      templateUrl: 'app/components/dos-games.component/dosgames.component.html',
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