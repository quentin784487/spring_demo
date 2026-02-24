(function () {
    'use strict';

    angular
        .module('myApp')
        .component('gamelistingComponent', {
            templateUrl: 'app/components/gamelisting.component/gamelisting.component.html',
            bindings: {
                title: '=',
                games: '<',
                alphabet: '<',
                onSelect: '&',
                onSearch: '&'
            },
            controller: function () {
                var vm = this;
                vm.select = function (letter) {
                    vm.onSelect({ letter: letter });
                };

                vm.search = function (title) {
                    vm.onSearch({ title: title });
                };
            }
        });
})();