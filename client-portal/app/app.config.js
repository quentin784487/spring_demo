(function () {
  'use strict';

  angular
    .module('myApp')
    .config(function ($stateProvider, $urlRouterProvider) {

      $urlRouterProvider.otherwise('/');

      $stateProvider
        .state('home', {
          url: '/',
          templateUrl: 'app/components/home.component/home.component.html',
          controller: 'MainController',
          controllerAs: 'vm'
        })
        .state('about', {
          url: '/about',
          templateUrl: 'app/views/about.html'
        });
    });
})();