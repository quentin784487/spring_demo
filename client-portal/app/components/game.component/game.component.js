(function () {
    'use strict';

    angular
        .module('myApp')
        .component('gameComponent', {
            templateUrl: 'app/components/game.component/game.component.html',
            bindings: {
                gameId: '<',
                onClose: '&'
            },
            controller: function (ApiService) {
                var vm = this;

                vm.limit = 400;
                vm.isExpanded = false;

                vm.$onInit = function () {
                    if (vm.gameId) {
                        vm.loadGame(vm.gameId);
                    }
                };

                vm.toggle = function () {
                    vm.isExpanded = !vm.isExpanded;
                };

                vm.loadGame = function (id) {
                    ApiService.getGame(id)
                        .then(function (response) {
                            vm.game = response.data;
                        })
                        .catch(function (error) {
                            console.error('API error:', error);
                        });
                };

                vm.close = function() {
                    vm.onClose();
                };
            }
        });
})();