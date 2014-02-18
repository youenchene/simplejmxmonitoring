
var services = angular.module('simplejmxmonitor-services', ['ngResource','ngRoute']);


services.factory('SensorService', function($resource) {
    return $resource('/sensor/:id',
        {id: "@id"},
        {update: {method:'PUT'}}
    );
});