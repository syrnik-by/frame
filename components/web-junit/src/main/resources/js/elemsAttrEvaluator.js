(function (arguments) {
     let attributeName = arguments[0];
     let iter = 0;
     let HeadingElements = arguments[1];
     let values = [];
     while (iter < arguments.length -1 ) {
       iter++;
       node = arguments[iter];
       values.push(node[`${attributeName}`]);
     };
     return values;
})(Array.from(arguments));