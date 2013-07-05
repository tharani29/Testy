Ext.onReady(function(){

    Ext.QuickTips.init();

    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    /*
     * ================  Simple form  =======================
     */
    bd.createChild({tag: 'h2', html: 'Field Components'});

    var simple = new Ext.FormPanel({
        labelWidth: 75,
        frame: true,
        title: 'Simple Form',
        bodyStyle:'padding:5px 5px 0',
        width: 350,
        defaults: {width: 230},

        items: [{
            xtype:'displayfield',
            name: 'displayField',
            value: 'DisplayFieldValue'
        },{
            xtype: 'datefield',
            name: 'dateField',
            editable : false,
            format: 'd/m/Y',
            minValue: '01/01/06'
        }, new Ext.form.TimeField({
            fieldLabel: 'Time',
            name: 'time',
            minValue: '8:00am',
            maxValue: '6:00pm'
        }), new Ext.Button({
            style:{
                fontSize:12,
                marginLeft: "100px"

            },
            html:'<a href="#" style="color: blue">Download File</a>',
            handler: function() {
                window.location.href = '../../resources/upload/text.docx';
            }

        })
        ],

        buttons: [{
            text: 'Download',
            handler: function() {
                window.location.href = '../../resources/upload/text.docx';
            }
        },{
            text: 'Cancel'
        }]
    });

    simple.render(document.body);
});