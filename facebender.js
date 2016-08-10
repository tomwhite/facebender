var board = JXG.JSXGraph.initBoard("box", {
	boundingbox: [0, 0, 500, 500],
	keepAspectRatio: true,
	showCopyright: false,
	showNavigation: false,
	axis: false
});

var points = [
[[135,145],],
[[190,145],],
[[128,144], [133,149], [140,144], [135,141], [128,144], ],
//[[184,144], [189,149], [196,144], [190,141], [184,144], ],
//[[119,147], [133,140], [147,146],],
//[[177,147], [190,141], [203,147],],
//[[121,147], [133,150], [147,146],],
//[[177,147], [191,150], [201,148],],
//[[118,143], [132,137], [148,142],],
//[[176,143], [191,137], [204,143],],
//[[127,154], [135,153], [144,150],],
//[[178,151], [187,154], [196,154],],
//[[156,140], [156,153], [156,165], [154,172], [156,179], [161,182],],
//[[166,140], [166,153], [166,166], [168,172], [167,179], [161,182],],
//[[150,169], [147,173], [146,178], [148,182], [153,179], [161,182],],
//[[173,169], [176,172], [177,178], [174,182], [170,179], [163,182],],
//[[112,137], [113,132], [125,127], [139,128], [150,131], [152,136],],
//[[171,136], [173,132], [186,129], [199,128], [208,132], [211,137],],
//[[112,138], [124,132], [138,134], [152,136],],
//[[171,136], [187,134], [200,132], [210,137],],
//[[137,203], [149,199], [156,196], [162,199], [168,197], [177,199], [187,202],],
//[[138,203], [148,203], [156,202], [163,203], [170,202], [178,202], [186,202],],
//[[138,203], [149,203], [156,202], [163,203], [170,202], [177,202], [186,203],],
//[[141,204], [148,207], [155,210], [163,211], [171,210], [179,207], [185,203],],
//[[103,141], [101,160], [104,181],],
//[[219,140], [222,159], [218,179],],
//[[99,150], [92,144], [88,149], [90,160], [94,174], [99,187], [104,184],],
//[[224,149], [231,144], [234,151], [232,160], [230,173], [224,185], [219,184],],
//[[104,181], [108,199], [115,214], [129,228], [147,240], [162,243], [180,239], [196,228], [207,215], [215,199], [219,178],]
//[[101,144], [107,129], [114,114], [120,104], [131,95], [146,92], [160,93], [174,95], [188,96], [201,103], [210,114], [217,
//[[93,204], [78,173], [76,142], [82,101], [99,70], [129,46], [158,44], [188,45], [217,64], [236,94], [245,134], [250,168],
//[[145,175], [139,182], [135,190],],
//[[178,176], [185,183], [190,191],],
//[[105,178], [109,184], [112,190],],
//[[218,178], [214,183], [211,189],],
//[[159,186], [159,193],],
//[[165,186], [165,193],],
//[[162,232], [162,238],],
//[[153,218], [162,216], [173,219],],
];

var features = [
"left pupil",
"right pupil",
"left iris",
//"right iris",
];

var averageFaceData = [];
var faceData = [];

var offsetX = 250;

function makeUpdater(feat, slider) {
	return function() {
		for (var i = 0; i < points[feat].length; i++) {
			var normX = points[feat][i][0];
			var normY = points[feat][i][1];
			var faceX = faceData[feat][i].X();
			var faceY = faceData[feat][i].Y();
			this.dataX[i] = faceX + slider.Value()*(faceX-normX) + offsetX;
			this.dataY[i] = faceY + slider.Value()*(faceY-normY);
		}
	};
};

var image;
var resizePoint;

// draw the background image
function drawImage(imgx, imgy, imgw, imgh, imageUrl, fixed, origin) {
  if (imageUrl != null) {
	if (imgw == 0) { // image hasn't been loaded before, so calculate its dimensions
	  $('body').append('<div id="init"><img id="spiralImage" src="' + imageUrl + '" /></div>');
	  $('#init').hide();
	  $('#spiralImage').bind("load",function() {
		  var imWidthPx = this.width;
		  var imHeightPx = this.height;
		  var widthPx = $('#box').width();
		  var heightPx = $('#box').height();
		  var boundingBox = board.getBoundingBox();
		  var minx = boundingBox[0];
		  var miny = boundingBox[1];
		  var maxx = boundingBox[2];
		  var maxy = boundingBox[3];
		  var width = minx + (maxx - minx) * imWidthPx/widthPx;
		  var height = miny + (maxy - miny) * imHeightPx/heightPx;
		  resizePoint = board.create('point', [width, height], { size:10, opacity:0.3, name:"" });
		  image = board.create('image', [imageUrl,
			[minx, function(){ return calculateAspectRatioFit(width, height, resizePoint).height; }],
			  [function(){ return calculateAspectRatioFit(width, height, resizePoint).width; }, function(){ return calculateAspectRatioFit(width, height, resizePoint).height; }] ], {fixed: fixed});
	  });
	} else {
	  image = board.create('image', [imageUrl, origin || [imgx, imgy], [imgw, imgh] ], {fixed: fixed});
	}
  }
}

function calculateAspectRatioFit(srcWidth, srcHeight, p) {
	var ratio = Math.min(p.X() / srcWidth, p.Y() / srcHeight);
	return { width: srcWidth*ratio, height: srcHeight*ratio };
}

function resizeDone() {
	board.removeObject(resizePoint);
	chooseNextFeature();
}

var featureIndex = 0;
var subFeatureIndex = 0;
var pointIndex = 0;
var downEnabled = true;

function chooseNextFeature() {
	if (featureIndex == 0) {
		// from http://jsxgraph.uni-bayreuth.de/wiki/index.php/Browser_event_and_coordinates
		var getMouseCoords = function(e, i) {
				var cPos = board.getCoordsTopLeftCorner(e, i),
					absPos = JXG.getPosition(e, i),
					dx = absPos[0]-cPos[0],
					dy = absPos[1]-cPos[1];

				return new JXG.Coords(JXG.COORDS_BY_SCREEN, [dx, dy], board);
			};
		var down = function(e) {
				if (!downEnabled) return;
				var canCreate = true, i, coords, el;

				if (e[JXG.touchProperty]) {
					// index of the finger that is used to extract the coordinates
					i = 0;
				}
				coords = getMouseCoords(e, i);

				for (el in board.objects) {
					if(JXG.isPoint(board.objects[el]) && board.objects[el].hasPoint(coords.scrCoords[1], coords.scrCoords[2])) {
						canCreate = false;
						break;
					}
				}

				if (canCreate) {
					// create a point on the image
					p = board.create('point', [coords.usrCoords[1], coords.usrCoords[2]], {name: '', size: 1, face: 'o'});
					if (subFeatureIndex == 0) {
						faceData.push([p]);
					} else {
						faceData[featureIndex].push(p);
					}

					// dim the corresponding point on the average face
					if (featureIndex > 1) {
						averageFaceData[featureIndex][subFeatureIndex].setProperty({opacity:0.3});
					}

					var subFeatureCount = points[featureIndex].length;
					if (++subFeatureIndex == subFeatureCount) {
						featureIndex++;
						subFeatureIndex = 0;
					}
					pointIndex++;
					if (featureIndex == 2 && subFeatureIndex == 0) {
						showAverageFace();
					}
					// highlight the next point on the average face
					if (featureIndex > 1 && featureIndex < features.length) {
						averageFaceData[featureIndex][subFeatureIndex].setProperty({visible:true});
					}
					if (featureIndex < features.length) {
						chooseNextFeature();
					} else {
						downEnabled = false;
						bendFace();
					}
				}
			};

		handlerId = board.on('down', down);
	}
	$('#controls').html('<p id="instruction">Choose ' + features[featureIndex] + ' (' + (subFeatureIndex + 1) + ' of ' + points[featureIndex].length + ')</p>');
}

function showAverageFace() {
	// TODO: transform so that face has eyes same distance apart, level with photo, and to the right of photo
	var translation = board.create('transform', [200, 0], {type:'translate'});
	for (var feature = 0; feature < points.length; feature++) {
		var featurePoints = points[feature];
		var p = [];
		var x = [];
		var y = [];
		// TODO: make eyes fixed so you can't move them
		for (var i = 0; i < featurePoints.length; i++) {
			p[i] = board.create('point', featurePoints[i], {name: '', size: 1, face: 'o', visible: false});
			translation.bindTo(p[i]);
			//x[i] = featurePoints[i][0] + offsetX;
			//y[i] = featurePoints[i][1];
		}
		averageFaceData[feature] = p;
		// TODO: create point for features of length 1 (pupils)
		for (var i = 1; i < p.length; i++) {
			board.create('segment', [p[i-1], p[i]]);
		}
//			var c = board.create('curve', [x, y]);
//			c.updateDataArray = makeUpdater(feature);
	}
}

function bendFace() {
	var slider = board.create('slider',[[0, 10],[100, 10],[0, 1, 5]]);
	for (var feature = 0; feature < points.length; feature++) {
		var featurePoints = points[feature];
		var x = [];
		var y = [];
		for (var i = 0; i < featurePoints.length; i++) {
			x[i] = featurePoints[i][0] + offsetX * 2;
			y[i] = featurePoints[i][1];
		}
		var c = board.create('curve', [x, y]);
		c.updateDataArray = makeUpdater(feature, slider);
	}
}

// TODO: open an image and allow face to be cropped
var imageUrl = "file:///Users/tom/projects-workspace/facebender/images/tom.jpg"
drawImage(0, 0, 0, 0, imageUrl, true, [0, 0]);

// TODO: save image
