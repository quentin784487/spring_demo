(function () {
  'use strict';

  angular
    .module('myApp')
    .controller('MainController', function (ApiService) {
      var vm = this;

      vm.games = [];
      vm.gameId = 0;
      vm.genres = [];
      vm.title = '';
      vm.selectedGenreId = 0;
      vm.letter = '';
      vm.alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
      vm.pageSize = 5;
      vm.totalPages = 0;
      vm.currentPage = 0;
      vm.activeComponent = 'list';

      vm.$onInit = function () {        
        vm.loadData(vm.currentPage, vm.pageSize, vm.title, "ALL", vm.selectedGenreId);
        vm.loadGenres();
      };

      vm.loadData = function (page, size, title, method, genre) {
        ApiService.getGames(page, size, title, method, genre)
          .then(function (response) {
            vm.games = response.data.content;
            vm.pageSize = response.data.pageable.pageSize;
            vm.totalPages = response.data.totalPages;
            vm.currentPage = response.data.pageable.pageNumber;
          })
          .catch(function (error) {
            console.error('API error:', error);
          });
      };

      vm.loadGenres = function () {
        ApiService.getGenres()
          .then(function (response) {
            vm.genres = response.data;
          })
          .catch(function (error) {
            console.error('API error:', error);
          });
      };

      vm.searchContaining = function (title) {
        vm.selectedGenreId = 0;
        vm.loadData(vm.currentPage, vm.pageSize, title, "CONTAINING", vm.selectedGenreId);
      }

      vm.searchStartsWith = function (letter) {
        vm.title = '';
        vm.loadData(vm.currentPage, vm.pageSize, letter, "STARTSWITH", vm.selectedGenreId);
      }

      vm.onGenreSelected = function (genre) {
        if (vm.activeComponent == 'details') {
          vm.activeComponent = 'list';
        }
        vm.title = '';
        vm.selectedGenreId = genre.id;
        vm.loadData(vm.currentPage, vm.pageSize, vm.title, "ALL", vm.selectedGenreId);
      };

      vm.setPage = function (page) {
        if (page >= 0 && page < vm.totalPages) {
          vm.currentPage = page;
          vm.loadData(vm.currentPage, vm.pageSize, vm.title, "ALL", vm.selectedGenreId);
        }
      };

      vm.selectGame = function (game) {
        vm.gameId = game.id;
        vm.activeComponent = 'details';
      };

      vm.showList = function () {
        vm.activeComponent = 'list';
      };      
    });
})();