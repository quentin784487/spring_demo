(function () {
    'use strict';

    angular
        .module('myApp')
        .component('gameListingComponent', {
            templateUrl: 'app/components/game-listing.component/game-listing.component.html',
            bindings: {
                title: '=',
                currentPage: '=',
                totalPages: '<',
                games: '<',
                alphabet: '<',
                onSelectLetter: '&',
                onSearch: '&',
                onPageChange: '&',
                onSelectGame: '&'
            },
            controller: function () {
                var vm = this;

                vm.selectLetter = function (letter) {
                    vm.onSelectLetter({ letter: letter });
                };

                vm.selectGame = function(game) {
                    vm.onSelectGame({ game: game });
                };

                vm.search = function (title) {
                    vm.onSearch({ title: title });
                };

                vm.setPage = function (page) {
                    vm.onPageChange({ page: page });
                };

                vm.$onChanges = function (changes) {
                    if (changes.totalPages && vm.totalPages) {
                        vm.pages = Array.from({ length: vm.totalPages }, (_, i) => i);
                    }
                };
            }
        });
})();