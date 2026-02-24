(function () {
  'use strict';

  angular
    .module('myApp')
    .controller('MainController', function (ApiService) {
      const pageSize = 10;
      var currentPage = 0;
      var vm = this;

      vm.games = [];
      vm.genres = [];
      vm.title = '';
      vm.selectedGenreId = 0;
      vm.letter = '';
      vm.alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");

      vm.loadData = function (page, size, title, method, genre) {
        ApiService.getGames(page, size, title, method, genre)
          .then(function (response) {
            vm.games = response.data.content;
          })
          .catch(function (error) {
            console.error('API error:', error);
          });
      };

      vm.searchContaining = function (title) {
        vm.selectedGenreId = 0;
        vm.loadData(currentPage, pageSize, title, "CONTAINING", vm.selectedGenreId);
      }

      vm.searchStartsWith = function (letter) {
        vm.title = '';
        vm.loadData(currentPage, pageSize, letter, "STARTSWITH", vm.selectedGenreId);
      }

      vm.onGenreSelected = function (genre) {
        vm.selectedGenreId = genre.id;
        vm.loadData(currentPage, pageSize, vm.title, "ALL", vm.selectedGenreId); 
      };

      vm.loadData(currentPage, pageSize, vm.title, "ALL", vm.selectedGenreId);      

      vm.loadGenres = function () {
        ApiService.getGenres()
          .then(function (response) {
            vm.genres = response.data;
          })
          .catch(function (error) {
            console.error('API error:', error);
          });
      };

      vm.loadGenres();
    });
})();