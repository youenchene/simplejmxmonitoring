function MainCtrl($scope, SensorService, timeFunctions) {

    $scope.sensors = SensorService.query();

    timeFunctions.$setInterval(function () {

        sensors= SensorService.query();

        $scope.sensors=sensors;}, 30000, $scope);

}

