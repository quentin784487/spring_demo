(function () {
  'use strict';

  angular
    .module('myApp')
    .config(function ($stateProvider, $urlRouterProvider) {

      $urlRouterProvider.otherwise('/');

      $stateProvider
        .state('main', {
          url: '/',
          templateUrl: 'app/components/main.component/main.component.html',
          controller: 'MainController',
          controllerAs: 'vm'
        })
        .state('about', {
          url: '/about',
          templateUrl: 'app/views/about.html'
        });
    });
})();