var board = JXG.JSXGraph.initBoard("box", {
	boundingbox: [0, 0, 500, 300],
	keepAspectRatio: true,
	showCopyright: false,
	showNavigation: false,
	axis: true
});

var pointSize = 0.5;

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
//[[104,181], [108,199], [115,214], [129,228], [147,240], [162,243], [180,239], [196,228], [207,215], [215,199], [219,178],],
//[[101,144], [107,129], [114,114], [120,104], [131,95], [146,92], [160,93], [174,95], [188,96], [201,103], [210,114], [217,126], [222,143],],
//[[93,204], [78,173], [76,142], [82,101], [99,70], [129,46], [158,44], [188,45], [217,64], [236,94], [245,134], [250,168], [233,200],],
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
	"Left Pupil",
	"Right Pupil",
	"Left Iris",
//	"Right Iris",
//	"Bottom of Left Eyelid",
//	"Bottom of Right Eyelid",
//	"Bottom of Left Eye",
//	"Bottom of Right Eye",
//	"Top of Left Eye",
//	"Top of Right Eye",
//	"Left Eye Line",
//	"Right Eye Line",
//	"Left Side of Nose",
//	"Right Side of Nose",
//	"Left Nostril",
//	"Right Nostril",
//	"Top of Left Eyebrow",
//	"Top of Right Eyebrow",
//	"Bottom of Left Eyebrow",
//	"Bottom of Right Eyebrow",
//	"Top of Upper Lip",
//	"Bottom of Upper Lip",
//	"Top of Lower Lip",
//	"Bottom of Lower Lip",
//	"Left Side of Face",
//	"Right Side of Face",
//	"Left Ear",
//	"Right Ear",
//	"Jaw",
//	"Hairline",
//	"Top of Head",
//	"Left Cheek Line",
//	"Right Cheek Line",
//	"Left Cheekbone",
//	"Right Cheekbone",
//	"Left Upper Lip Line",
//	"Right Upper Lip Line",
//	"Chin Cleft",
//	"Chin Line",
];

var averageFaceData = [];
var averageFaceSegments = [];
var faceData = [];
var faceDataTransformed = [];

function makeUpdater(feat, slider) {
	return function() {
		for (var i = 0; i < points[feat].length; i++) {
			var normX = points[feat][i][0];
			var normY = points[feat][i][1];
			var faceX = faceDataTransformed[feat][i].X();
			var faceY = faceDataTransformed[feat][i].Y();
			this.dataX[i] = faceX + slider.Value()*(faceX-normX);
			this.dataY[i] = faceY + slider.Value()*(faceY-normY);
		}
	};
};

var image;

// draw the background image
function drawImage(imageUrl) {
	$('body').append('<div id="init"><img id="faceImage" src="' + imageUrl + '" /></div>');
	$('#init').hide();
	$('#faceImage').bind("load",function() {
	  var imWidthPx = this.width;
	  var imHeightPx = this.height;
	  var widthPx = $('#box').width() / 2;
	  var heightPx = $('#box').height();
	  var boundingBox = board.getBoundingBox();
	  var minx = boundingBox[0];
	  var miny = boundingBox[1];
	  var maxx = boundingBox[2];
	  var maxy = boundingBox[3];
	  var width = (maxx - minx) * imWidthPx/widthPx;
	  var height = (maxy - miny) * imHeightPx/heightPx;
	  var w = (maxx - minx)/2;
	  var h = maxy - miny;
	  var c = calculateAspectRatioFit(width/2, height, w, h);
	  image = board.create('image', [imageUrl,
		[minx + w/2 - c.width/2 + w, c.height], [c.width, c.height] ], {fixed: true});
	});
}

// http://stackoverflow.com/questions/3971841/how-to-resize-images-proportionally-keeping-the-aspect-ratio
function calculateAspectRatioFit(srcWidth, srcHeight, maxWidth, maxHeight) {
    var ratio = Math.min(maxWidth / srcWidth, maxHeight / srcHeight);
    return { width: srcWidth*ratio, height: srcHeight*ratio };
}

function resizeDone() {
	showAverageFace();
	chooseNextFeature();
}

var featureIndex = 0;
var subFeatureIndex = 0;
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

//				for (el in board.objects) {
//					if(JXG.isPoint(board.objects[el]) && board.objects[el].hasPoint(coords.scrCoords[1], coords.scrCoords[2])) {
//						canCreate = false;
//						break;
//					}
//				}

				if (canCreate) {
					// create a point on the image
					// TODO: make tooltip show name of feature (https://groups.google.com/forum/#!topic/jsxgraph/HMObRq6W_GQ)
					// See also https://groups.google.com/forum/#!topic/jsxgraph/BD2LsWUFppk
					p = board.create('point', [coords.usrCoords[1], coords.usrCoords[2]], {name: '', size: pointSize, face: 'o'});
					if (subFeatureIndex == 0) {
						faceData.push([p]);
					} else {
						faceData[featureIndex].push(p);
					}

					// dim the corresponding point on the average face
					averageFaceData[featureIndex][subFeatureIndex].setProperty({opacity:0.3});

					var subFeatureCount = points[featureIndex].length;
					if (++subFeatureIndex == subFeatureCount) {
						featureIndex++;
						subFeatureIndex = 0;
					}
					if (featureIndex < features.length) {
						// highlight the next point on the average face
						averageFaceData[featureIndex][subFeatureIndex].setProperty({visible:true});
						chooseNextFeature();
					} else {
						downEnabled = false;
						bendFace();
					}
				}
			};

		board.on('down', down);
	}
	$('#controls').html('<p id="instruction">Choose ' + features[featureIndex] + ' (' + (subFeatureIndex + 1) + ' of ' + points[featureIndex].length + ')</p>');
}

function showAverageFace() {
	var translation = board.create('transform', [0, 0], {type:'translate'});
	for (var feature = 0; feature < points.length; feature++) {
		var featurePoints = points[feature];
		var p = [];
		var x = [];
		var y = [];
		for (var i = 0; i < featurePoints.length; i++) {
			p[i] = board.create('point', featurePoints[i], {name: '', size: pointSize, face: 'o', visible: false, fixed: true});
			translation.bindTo(p[i]);
		}
		averageFaceData[feature] = p;
		if (p.length == 1) { // create point for features of length 1 (pupils)
			var point = board.create('point', [p[0].X(), p[0].Y()], {name: '', size: pointSize, face: 'o', color: 'blue', fixed: true});
			// don't add to averageFaceSegments as the pupils are fixed
		} else {
			// TODO: use splines: http://jsxgraph.uni-bayreuth.de/wiki/index.php/Category:Interpolation
			//var segment = board.create('curve', JXG.Math.Numerics.CatmullRomSpline(p));
			//averageFaceSegments.push(segment);
			for (var i = 1; i < p.length; i++) {
				var segment = board.create('segment', [p[i-1], p[i]]);
				averageFaceSegments.push(segment);
			}
		}
	}
}

function bendFace() {

    var lp = faceData[0][0];
    var rp = faceData[1][0];

	// translate left pupil to origin
	var t1 = board.create('transform', [-lp.X(), -lp.Y()], {type:'translate'});
	// scale so that distance between eyes is the same as average face
	var factor = JXG.Math.Geometry.distance(points[0][0], points[1][0]) / JXG.Math.Geometry.distance([lp.X(), lp.Y()], [rp.X(), rp.Y()]);
	var t2 = board.create('transform', [factor, factor], {type:'scale'});
	// translate back to average left pupil
	var t3 = board.create('transform', points[0][0], {type:'translate'});

	var slider = board.create('slider',[[0, 10],[100, 10],[-1, -1, 5]], {snapWidth: 0.2});
	for (var feature = 0; feature < points.length; feature++) {
		var faceFeaturePoints = faceData[feature];
		var x = [];
		var y = [];
		var faceFeaturePointsTransformed = [];
		for (var i = 0; i < faceFeaturePoints.length; i++) {
			var fp = faceFeaturePoints[i];
			var fpt = board.create('point', [fp, [t1, t2, t3]], {color: 'green', visible: false});
			x[i] = fpt.X();
			y[i] = fpt.Y();
			faceFeaturePointsTransformed[i] = fpt;

			// hide average face points
			averageFaceData[feature][i].setProperty({visible: false});
		}
		faceDataTransformed[feature] = faceFeaturePointsTransformed;
		var c = board.create('curve', [x, y]); // TODO: use splines
		c.updateDataArray = makeUpdater(feature, slider);

	}

	// hide average face segments
	for (var i = 0; i < averageFaceSegments.length; i++) {
		averageFaceSegments[i].setProperty({visible: false});
	}

	var json = JSON.stringify(toPointsArray(faceDataTransformed));
	// TODO: store JSON in a file or in local storage

	$('#controls').html('<p id="instruction">Facebender</p>');
}

/*
 * Convert a nested array of points to simple points (two-element arrays of double).
 */
function toPointsArray(jsxgraphPoints) {
	return jsxgraphPoints.map(function(arr) {
		return arr.map(function(p){return [p.X(), p.Y()];});
	});
}

// TODO: open an image and allow face to be cropped (do cropping externally)
var imageUrl = "file:///Users/tom/projects-workspace/facebender/images/tom.jpg"
drawImage(imageUrl);

// TODO: save image

// TODO: use two linked boards http://jsxgraph.uni-bayreuth.de/wiki/index.php/Plot_data_with_slider
// TODO: make it easy to have a cut down number of points for prototyping
